package com.schoolapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.schoolapp.dao.BatchHeaderDto;
import com.schoolapp.entity.BatchDetails;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.entity.CustomerTrnDetails;
import com.schoolapp.repository.BatchDetailsRepository;
import com.schoolapp.repository.CustomerTrnDetailsRepository;
import com.schoolapp.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchDetailsServiceImpl implements BatchDetailsService {

    @Autowired
    private BatchDetailsRepository batchRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerTrnDetailsRepository trnRepo;

    @Autowired
    private JavaMailSender mailSender;

    // --------------------------
    // CREATE BATCH
    // --------------------------
    @Override
    public BatchDetails createBatch(BatchDetails batch) {
        batch.setApprovalStage("NONE");
        return batchRepo.save(batch);
    }

    // --------------------------
    // GET ALL BATCHES (NO ROLES)
    // --------------------------
    @Override
    @Transactional(readOnly = true)
    public List<BatchHeaderDto> getAllBatches() {

        List<BatchDetails> list = batchRepo.findAll();

        return list.stream().map(batch -> {
            BatchHeaderDto dto = new BatchHeaderDto();
            dto.setBactno(batch.getBactno());
            dto.setTrndate(batch.getTrndate() != null ? batch.getTrndate().toString() : null);
            dto.setCreatedby(batch.getCreatedby());
            dto.setTotalTransactions(batch.getTransactions() == null ? 0 : batch.getTransactions().size());
            dto.setApprovalStage(batch.getApprovalStage());

            batch.setAproval1Name(resolveUserName(batch.getAproval1()));
            batch.setAproval2Name(resolveUserName(batch.getAproval2()));
            batch.setAproval3Name(resolveUserName(batch.getAproval3()));

            dto.setAproval1Name(batch.getAproval1Name());
            dto.setAproval2Name(batch.getAproval2Name());
            dto.setAproval3Name(batch.getAproval3Name());

            return dto;
        }).collect(Collectors.toList());
    }

    // --------------------------
    // APPROVE WORKFLOW (NO ROLES)
    // --------------------------
    @Override
    public BatchDetails approveBatch(Long bactno, Long userId, String role) {

        BatchDetails batch = batchRepo.findById(bactno)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        String stage = batch.getApprovalStage();

        switch (stage) {
            case "NONE":
                batch.setAproval1(String.valueOf(userId));
                batch.setApprovalStage("L1");
                break;

            case "L1":
                batch.setAproval2(String.valueOf(userId));
                batch.setApprovalStage("L2");
                break;

            case "L2":
                batch.setAproval3(String.valueOf(userId));
                batch.setApprovalStage("L3");
                break;

            case "L3":
                throw new RuntimeException("Already fully approved");
        }

        BatchDetails saved = batchRepo.save(batch);
        sendApprovalEmails(saved);
        return saved;
    }

    // --------------------------
    // REJECT WORKFLOW (NO ROLES)
    // --------------------------
    @Override
    public BatchDetails rejectBatch(Long bactno, Long userId, String role, String reason) {

        BatchDetails batch = batchRepo.findById(bactno)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        if ("L3".equals(batch.getApprovalStage())) {
            throw new RuntimeException("Already approved");
        }

        batch.setApprovalStage("NONE");
        batch.setAproval1(null);
        batch.setAproval2(null);
        batch.setAproval3(null);
        batch.setAproval4(String.valueOf(userId));

        BatchDetails saved = batchRepo.save(batch);
        sendRejectionEmails(saved, reason);
        return saved;
    }

    // --------------------------
    // UPDATE BATCH
    // --------------------------
    @Override
    public BatchDetails updateBatch(Long bactno, BatchDetails update, Long userId, String role) {

        BatchDetails batch = batchRepo.findById(bactno)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batch.setTrndate(update.getTrndate());
        batch.setCreatedby(update.getCreatedby());

        return batchRepo.save(batch);
    }

    // --------------------------
    // DELETE BATCH
    // --------------------------
    @Override
    public void deleteBatch(Long bactno, Long userId, String role) {

        BatchDetails batch = batchRepo.findById(bactno)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batchRepo.delete(batch);
    }

    // --------------------------
    // EMAIL HELPERS (SEND TO ALL USERS)
    // --------------------------
    private void sendApprovalEmails(BatchDetails b) {

        List<String> emails = userRepository.findAll().stream()
                .map(UserEntity::getEmail)

                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        sendSimpleEmail(
                emails.toArray(new String[0]),
                "Batch " + b.getBactno() + " Approved",
                "Batch No " + b.getBactno() + " approved at stage " + b.getApprovalStage());
    }

    private void sendRejectionEmails(BatchDetails b, String reason) {

        List<String> emails = userRepository.findAll().stream()
                .map(UserEntity::getEmail)

                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        sendSimpleEmail(
                emails.toArray(new String[0]),
                "Batch " + b.getBactno() + " Rejected",
                "Reason: " + (reason == null ? "" : reason));
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
    // UTILS
    // --------------------------
    private String resolveUserName(String userId) {
        try {
            if (userId == null || userId.isEmpty())
                return "";
            return userRepository.findById(Long.parseLong(userId))
                    .map(UserEntity::getUsername)

                    .orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public List<CustomerTrnDetails> getTransactions(Long bactno) {
        return trnRepo.findByTrbactno(bactno);
    }

    @Override
    public CustomerTrnDetails addTransaction(Long bactno, CustomerTrnDetails trn) {
        BatchDetails batch = batchRepo.findById(bactno)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        trn.setBatchDetails(batch);
        trn.setTrbactno(bactno);
        return trnRepo.save(trn);
    }

    @Override
    public CustomerTrnDetails updateTransaction(Long id, CustomerTrnDetails trn) {
        CustomerTrnDetails existing = trnRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        existing.setCustomerName(trn.getCustomerName());
        existing.setToolingdrawingpartno(trn.getToolingdrawingpartno());
        existing.setPartdrawingname(trn.getPartdrawingname());
        existing.setPartdrawingno(trn.getPartdrawingno());
        existing.setDescriptionoftooling(trn.getDescriptionoftooling());
        existing.setCmworkorderno(trn.getCmworkorderno());
        existing.setToolingassetno(trn.getToolingassetno());

        return trnRepo.save(existing);
    }

    @Override
    public void deleteTransaction(Long id) {
        trnRepo.deleteById(id);
    }
}
