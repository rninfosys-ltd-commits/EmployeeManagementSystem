package com.schoolapp.service;

import org.springframework.web.server.ResponseStatusException;

import com.schoolapp.entity.KmBatch;
import com.schoolapp.entity.KmDetails;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.KmBatchRepository;
import com.schoolapp.repository.KmRepository;
import com.schoolapp.repository.UserRepository;

import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

//import javax.lang.model.element.Element;

@Service
public class KmBatchServiceImpl implements KmBatchService {

    @Autowired
    private KmBatchRepository kmBatchRepository;

    @Autowired
    private KmRepository kmRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    // --------------------------
    // CREATE BATCH
    // --------------------------
    @Override
    public KmBatch createBatch(KmBatch batch) {
        if (batch.getApprovalStage() == null)
            batch.setApprovalStage("NONE");
        return kmBatchRepository.save(batch);
    }

    // --------------------------
    // ADD ENTRY (associate with batch)
    // --------------------------
    @Override
    @Transactional
    public KmDetails addEntry(Long batchNo, KmDetails entry) {
        KmBatch batch = kmBatchRepository.findById(batchNo)
                .orElseThrow(() -> new RuntimeException("KM Batch not found: " + batchNo));
        // associate and save
        entry.setBatch(batch);
        KmDetails saved = kmRepository.save(entry);
        return saved;
    }

    // --------------------------
    // GET ALL KM BATCHES (role-aware)
    // --------------------------

    @Override
    public List<KmBatch> getAllBatches() {

        List<KmBatch> list = kmBatchRepository.findAll();

        list.forEach(b -> {
            b.setApproval1Name(resolveUserName(b.getApproval1()));
            b.setApproval2Name(resolveUserName(b.getApproval2()));
            b.setApproval3Name(resolveUserName(b.getApproval3()));
        });

        return list;
    }

    // --------------------------
    // GET ENTRIES BY BATCH
    // --------------------------
    @Override
    public List<KmDetails> getEntries(Long batchNo) {
        return kmRepository.findByBatch_KmBatchNo(batchNo);
    }

    // --------------------------
    // APPROVE WORKFLOW — L1 → L2 → L3
    // --------------------------
    @Override
    @Transactional
    public KmBatch approveBatch(Long batchNo, Long userId, String role) {

        KmBatch batch = kmBatchRepository.findById(batchNo)
                .orElseThrow(() -> new RuntimeException("KM Batch not found"));

        String stage = batch.getApprovalStage() == null ? "NONE" : batch.getApprovalStage();

        switch (stage) {

            case "NONE": // L1 → L2
                if (!"ROLE_DIRECTOR".equals(role))
                    throw new RuntimeException("You do not have authority to perform this action.");
                batch.setApproval1(String.valueOf(userId));
                batch.setApprovalStage("L1");
                break;

            case "L1": // L2 → L3
                if (!"ROLE_MANAGER".equals(role))
                    throw new RuntimeException("You do not have authority to perform this action.");
                batch.setApproval2(String.valueOf(userId));
                batch.setApprovalStage("L2");
                break;

            case "L2": // L3 final approve
                if (!"ROLE_SUPERVISOR".equals(role))
                    throw new RuntimeException("You do not have authority to perform this action.");
                batch.setApproval3(String.valueOf(userId));
                batch.setApprovalStage("L3");
                break;

            case "L3":
                throw new RuntimeException("Already fully approved");
            default:
                throw new RuntimeException("Cannot approve batch in stage: " + stage);
        }

        KmBatch saved = kmBatchRepository.save(batch);

        // send approval emails
        sendApprovalEmails(saved);

        return saved;
    }

    // --------------------------
    // REJECT WORKFLOW (same authority pattern as BatchDetails)
    // --------------------------
    @Override
    @Transactional
    public KmBatch rejectBatch(Long batchNo, Long userId, String role, String reason) {

        KmBatch batch = kmBatchRepository.findById(batchNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch not found"));

        String stage = batch.getApprovalStage() == null ? "NONE" : batch.getApprovalStage();

        // Fully approved cannot reject
        if ("L3".equals(stage)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_AUTH");
        }

        // User who approved cannot reject
        if (String.valueOf(userId).equals(batch.getApproval1()) ||
                String.valueOf(userId).equals(batch.getApproval2()) ||
                String.valueOf(userId).equals(batch.getApproval3())) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_AUTH");
        }

        // Authority logic
        switch (stage) {
            case "NONE":
                if (!"ROLE_DIRECTOR".equals(role))
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_AUTH");
                break;

            case "L1":
                if (!"ROLE_MANAGER".equals(role))
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_AUTH");
                break;

            case "L2":
                if (!"ROLE_SUPERVISOR".equals(role))
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_AUTH");
                break;

            default:
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_AUTH");
        }

        // Valid rejection
        batch.setApprovalStage("NONE");
        batch.setApproval1(null);
        batch.setApproval2(null);
        batch.setApproval3(null);
        batch.setApproval4(String.valueOf(userId));

        KmBatch saved = kmBatchRepository.save(batch);

        sendRejectionEmails(saved, role, userId, reason);

        return saved;
    }

    // --------------------------
    // DELETE BATCH (ADMIN / CREATOR only)
    // --------------------------
    @Override
    @Transactional
    public void deleteBatch(Long batchNo) {
        if (!kmBatchRepository.existsById(batchNo)) {
            throw new RuntimeException("KM Batch not found");
        }

        String currentRole = null;
        try {
            // UserDetailsImpl u = (UserDetailsImpl) auth.getPrincipal();
            // currentUserId = u.getId();
            // currentRole = u.getRole();
        } catch (Exception ignored) {
        }

        if ("ROLE_COMPANY_OWNER".equals(currentRole)) {
            // allow admin
            // also delete entries
            List<KmDetails> entries = kmRepository.findByBatch_KmBatchNo(batchNo);
            if (entries != null) {
                entries.forEach(e -> kmRepository.deleteById(e.getId()));
            }
            kmBatchRepository.deleteById(batchNo);
            return;
        }

        // // allow creator only
        // if (currentUserId != null) {
        // User user = userRepository.findById(currentUserId)
        // .orElseThrow(() -> new RuntimeException("User not found"));
        // String creator = batch.getCreatedby() == null ? "" : batch.getCreatedby();
        // if (creator.equals(user.getUsername())) {
        // List<KmDetails> entries = kmRepository.findByBatch_KmBatchNo(batchNo);
        // if (entries != null) {
        // entries.forEach(e -> kmRepository.deleteById(e.getId()));
        // }
        // kmBatchRepository.deleteById(batchNo);
        // return;
        // }
        // }

        throw new RuntimeException("You do not have authority to perform this action.");
    }

    private void sendApprovalEmails(KmBatch b) {
        String stage = b.getApprovalStage();
        Set<String> recipients = new HashSet<>();
        recipients.addAll(getEmailsByRole("ROLE_COMPANY_OWNER"));

        if ("L2".equals(stage)) {
            recipients.addAll(getEmailsByRole("ROLE_DIRECTOR"));
        }
        if ("L3".equals(stage)) {
            recipients.addAll(getEmailsByRole("ROLE_DIRECTOR"));
            recipients.addAll(getEmailsByRole("ROLE_MANAGER"));
        }

        sendSimpleEmail(recipients.toArray(new String[0]),
                "KM Batch " + b.getKmBatchNo() + " Approved (" + stage + ")",
                "KM Batch " + b.getKmBatchNo() + " has been approved at stage " + stage + ".");
    }

    private void sendRejectionEmails(KmBatch b, String role, Long userId, String reason) {
        Set<String> r = new HashSet<>();
        r.addAll(getEmailsByRole("ROLE_COMPANY_OWNER"));

        if ("ROLE_MANAGER".equals(role)) {
            r.addAll(getEmailsByRole("ROLE_DIRECTOR"));
        }
        if ("ROLE_SUPERVISOR".equals(role)) {
            r.addAll(getEmailsByRole("ROLE_DIRECTOR"));
            r.addAll(getEmailsByRole("ROLE_MANAGER"));
        }

        sendSimpleEmail(r.toArray(new String[0]),
                "KM Batch " + b.getKmBatchNo() + " Rejected by " + role,
                "Reason: " + (reason == null ? "" : reason));
    }

    private List<String> getEmailsByRole(String role) {
        return userRepository.findAll().stream()
                .filter(u -> role.equals(u.getRole()))
                .map(UserEntity::getEmail)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void sendSimpleEmail(String[] to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
        } catch (Exception ignored) {
        }
    }

    // --------------------------
    // Utilities
    // --------------------------
    private String resolveUserName(String userIdStr) {
        try {
            if (userIdStr == null || userIdStr.isEmpty())
                return "";
            Long id = Long.parseLong(userIdStr);
            return userRepository.findById(id).map(UserEntity::getUsername).orElse("");

        } catch (Exception e) {
            return "";
        }
    }

}
