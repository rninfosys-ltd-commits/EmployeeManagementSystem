package com.schoolapp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.schoolapp.dto.TransactionExportRequest;
import com.schoolapp.entity.PurchaseOrders;
import com.schoolapp.entity.SellOrder;
import com.schoolapp.entity.UserEntity;
import com.schoolapp.repository.PurchaseOrdersRepository;
import com.schoolapp.repository.SellOrderRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionReportService {

    @Autowired
    private SellOrderRepository sellRepo;

    @Autowired
    private PurchaseOrdersRepository purchaseRepo;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateTimeFormatter ldtFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat amountFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    static {
        // Ensure any extra custom rounding or formatting needs are met
    }

    public byte[] exportTransactions(TransactionExportRequest request) throws Exception {
        if ("SALES".equalsIgnoreCase(request.getType())) {
            List<SellOrder> sales = sellRepo.findAll(getSalesSpecification(request));
            if ("excel".equalsIgnoreCase(request.getFormat())) {
                return generateSalesExcel(sales, request);
            } else {
                return generateSalesPdf(sales, request);
            }
        } else {
            List<PurchaseOrders> purchases = purchaseRepo.findAll(getPurchaseSpecification(request));
            if ("excel".equalsIgnoreCase(request.getFormat())) {
                return generatePurchaseExcel(purchases, request);
            } else {
                return generatePurchasePdf(purchases, request);
            }
        }
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null)
            return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Specification<SellOrder> getSalesSpecification(TransactionExportRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), toLocalDateTime(request.getFromDate())));
            }
            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"),
                        toLocalDateTime(request.getToDate()).plusHours(23).plusMinutes(59)));
            }
            if (request.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), request.getMinAmount().longValue()));
            }
            if (request.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), request.getMaxAmount().longValue()));
            }
            if (request.getPartyName() != null && !request.getPartyName().isEmpty()) {
                Join<SellOrder, UserEntity> userJoin = root.join("user");
                predicates.add(cb.like(userJoin.get("username"), "%" + request.getPartyName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<PurchaseOrders> getPurchaseSpecification(TransactionExportRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), toLocalDateTime(request.getFromDate())));
            }
            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"),
                        toLocalDateTime(request.getToDate()).plusHours(23).plusMinutes(59)));
            }
            if (request.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), request.getMinAmount().longValue()));
            }
            if (request.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), request.getMaxAmount().longValue()));
            }
            if (request.getPartyName() != null && !request.getPartyName().isEmpty()) {
                Join<PurchaseOrders, UserEntity> userJoin = root.join("user");
                predicates.add(cb.like(userJoin.get("username"), "%" + request.getPartyName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private byte[] generateSalesExcel(List<SellOrder> data, TransactionExportRequest request) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sales Transactions");

            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            CellStyle orderHeaderStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font orderFont = workbook.createFont();
            orderFont.setBold(true);
            orderHeaderStyle.setFont(orderFont);
            orderHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            orderHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            orderHeaderStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle itemHeaderStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font itemFontFont = workbook.createFont();
            itemFontFont.setBold(true);
            itemFontFont.setFontHeightInPoints((short) 9);
            itemHeaderStyle.setFont(itemFontFont);
            itemHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            itemHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            itemHeaderStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle itemDataStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font itemDataFont = workbook.createFont();
            itemDataFont.setFontHeightInPoints((short) 9);
            itemDataStyle.setFont(itemDataFont);
            itemDataStyle.setBorderBottom(BorderStyle.HAIR);

            CellStyle amountStyle = workbook.createCellStyle();
            amountStyle.setDataFormat(workbook.createDataFormat().getFormat("[$-en-IN] #,##,##,##0.00"));
            amountStyle.setBorderBottom(BorderStyle.THIN);

            int rowIdx = 0;
            String dateRange = (request.getFromDate() != null && request.getToDate() != null)
                    ? dateFormat.format(request.getFromDate()) + " to " + dateFormat.format(request.getToDate())
                    : "All Time";
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Sales Transaction Report (" + dateRange + ")");
            titleCell.setCellStyle(titleStyle);

            rowIdx++; // Spacer

            double grandTotal = 0;
            for (SellOrder o : data) {
                // Order Info Row
                Row orderRow = sheet.createRow(rowIdx++);
                orderRow.createCell(0).setCellValue("Order ID: " + o.getId());
                orderRow.createCell(1)
                        .setCellValue("Date: " + (o.getDate() != null ? o.getDate().format(ldtFormat) : ""));
                orderRow.createCell(2)
                        .setCellValue("Party: " + (o.getUser() != null ? o.getUser().getUsername() : "N/A"));
                orderRow.createCell(3)
                        .setCellValue("Status: " + (o.getOrderStatus() != null ? o.getOrderStatus().toString() : ""));
                orderRow.createCell(4)
                        .setCellValue("Total: " + (o.getAmount() != null ? o.getAmount().doubleValue() : 0.0));
                for (int i = 0; i < 5; i++)
                    orderRow.getCell(i).setCellStyle(orderHeaderStyle);

                // Items Header
                Row itemHeader = sheet.createRow(rowIdx++);
                itemHeader.createCell(1).setCellValue("Product Name");
                itemHeader.createCell(2).setCellValue("Qty");
                itemHeader.createCell(3).setCellValue("Unit Price");
                itemHeader.createCell(4).setCellValue("Subtotal");
                for (int i = 1; i < 5; i++)
                    itemHeader.getCell(i).setCellStyle(itemHeaderStyle);

                // Items Data
                if (o.getCartItems() != null) {
                    for (com.schoolapp.entity.SellCartItems item : o.getCartItems()) {
                        Row itemRow = sheet.createRow(rowIdx++);
                        itemRow.createCell(1).setCellValue(item.getProductName());
                        itemRow.createCell(2).setCellValue(item.getQuantity() != null ? item.getQuantity() : 0);
                        itemRow.createCell(3)
                                .setCellValue(item.getPrice() != null ? item.getPrice().doubleValue() : 0.0);
                        itemRow.createCell(4).setCellValue(
                                item.getTotalAmount() != null ? item.getTotalAmount().doubleValue() : 0.0);
                        for (int i = 1; i < 5; i++)
                            itemRow.getCell(i).setCellStyle(itemDataStyle);
                    }
                }
                rowIdx++; // Spacer between orders
                grandTotal += (o.getAmount() != null ? o.getAmount().doubleValue() : 0.0);
            }

            rowIdx++;
            Row summaryRow = sheet.createRow(rowIdx++);
            summaryRow.createCell(0).setCellValue("Total Records: " + data.size());
            summaryRow.createCell(3).setCellValue("Grand Total:");
            Cell grandTotalCell = summaryRow.createCell(4);
            grandTotalCell.setCellValue(grandTotal);
            grandTotalCell.setCellStyle(amountStyle);

            for (int i = 0; i < 5; i++)
                sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generatePurchaseExcel(List<PurchaseOrders> data, TransactionExportRequest request) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Purchase Transactions");

            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            CellStyle orderHeaderStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font orderFont = workbook.createFont();
            orderFont.setBold(true);
            orderHeaderStyle.setFont(orderFont);
            orderHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            orderHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            orderHeaderStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle itemHeaderStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font itemFontFont = workbook.createFont();
            itemFontFont.setBold(true);
            itemFontFont.setFontHeightInPoints((short) 9);
            itemHeaderStyle.setFont(itemFontFont);
            itemHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            itemHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            itemHeaderStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle itemDataStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font itemDataFont = workbook.createFont();
            itemDataFont.setFontHeightInPoints((short) 9);
            itemDataStyle.setFont(itemDataFont);
            itemDataStyle.setBorderBottom(BorderStyle.HAIR);

            CellStyle amountStyle = workbook.createCellStyle();
            amountStyle.setDataFormat(workbook.createDataFormat().getFormat("[$-en-IN] #,##,##,##0.00"));
            amountStyle.setBorderBottom(BorderStyle.THIN);

            int rowIdx = 0;
            String dateRange = (request.getFromDate() != null && request.getToDate() != null)
                    ? dateFormat.format(request.getFromDate()) + " to " + dateFormat.format(request.getToDate())
                    : "All Time";
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Purchase Transaction Report (" + dateRange + ")");
            titleCell.setCellStyle(titleStyle);

            rowIdx++; // Spacer

            double grandTotal = 0;
            for (PurchaseOrders o : data) {
                // Order Info Row
                Row orderRow = sheet.createRow(rowIdx++);
                orderRow.createCell(0).setCellValue("Order ID: " + o.getId());
                orderRow.createCell(1)
                        .setCellValue("Date: " + (o.getDate() != null ? o.getDate().format(ldtFormat) : ""));
                orderRow.createCell(2)
                        .setCellValue("Party: " + (o.getUser() != null ? o.getUser().getUsername() : "N/A"));
                orderRow.createCell(3)
                        .setCellValue("Status: " + (o.getOrderStatus() != null ? o.getOrderStatus().toString() : ""));
                orderRow.createCell(4)
                        .setCellValue("Total: " + (o.getAmount() != null ? o.getAmount().doubleValue() : 0.0));
                for (int i = 0; i < 5; i++)
                    orderRow.getCell(i).setCellStyle(orderHeaderStyle);

                // Items Header
                Row itemHeader = sheet.createRow(rowIdx++);
                itemHeader.createCell(1).setCellValue("Product Name");
                itemHeader.createCell(2).setCellValue("Qty");
                itemHeader.createCell(3).setCellValue("Unit Price");
                itemHeader.createCell(4).setCellValue("Subtotal");
                for (int i = 1; i < 5; i++)
                    itemHeader.getCell(i).setCellStyle(itemHeaderStyle);

                // Items Data
                if (o.getCartItems() != null) {
                    for (com.schoolapp.entity.PurchaseCartItems item : o.getCartItems()) {
                        Row itemRow = sheet.createRow(rowIdx++);
                        itemRow.createCell(1).setCellValue(item.getProductName());
                        itemRow.createCell(2).setCellValue(item.getQuantity() != null ? item.getQuantity() : 0);
                        itemRow.createCell(3)
                                .setCellValue(item.getPrice() != null ? item.getPrice().doubleValue() : 0.0);
                        itemRow.createCell(4).setCellValue(
                                item.getTotalAmount() != null ? item.getTotalAmount().doubleValue() : 0.0);
                        for (int i = 1; i < 5; i++)
                            itemRow.getCell(i).setCellStyle(itemDataStyle);
                    }
                }
                rowIdx++; // Spacer
                grandTotal += (o.getAmount() != null ? o.getAmount().doubleValue() : 0.0);
            }

            rowIdx++;
            Row summaryRow = sheet.createRow(rowIdx++);
            summaryRow.createCell(0).setCellValue("Total Records: " + data.size());
            summaryRow.createCell(3).setCellValue("Grand Total:");
            Cell grandTotalCell = summaryRow.createCell(4);
            grandTotalCell.setCellValue(grandTotal);
            grandTotalCell.setCellStyle(amountStyle);

            for (int i = 0; i < 5; i++)
                sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateSalesPdf(List<SellOrder> data, TransactionExportRequest request) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(44, 62, 80));
        Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Font orderInfoFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(33, 97, 140));
        Font headFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

        Paragraph title = new Paragraph("Sales Transaction Detail Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        String dateRange = (request.getFromDate() != null && request.getToDate() != null)
                ? dateFormat.format(request.getFromDate()) + " - " + dateFormat.format(request.getToDate())
                : "All Time";
        Paragraph subTitle = new Paragraph("Date Range: " + dateRange, subTitleFont);
        subTitle.setAlignment(Element.ALIGN_CENTER);
        subTitle.setSpacingAfter(20);
        document.add(subTitle);

        double grandTotal = 0;
        for (SellOrder o : data) {
            // Order Summary Bar
            PdfPTable headerTable = new PdfPTable(4);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(10);
            headerTable.setWidths(new int[] { 3, 5, 3, 3 });

            PdfPCell c1 = new PdfPCell(new Phrase("Order ID: " + o.getId(), orderInfoFont));
            c1.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c1);

            PdfPCell c2 = new PdfPCell(
                    new Phrase("Party: " + (o.getUser() != null ? o.getUser().getUsername() : "N/A"), orderInfoFont));
            c2.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(
                    new Phrase("Date: " + (o.getDate() != null ? o.getDate().format(ldtFormat) : ""), orderInfoFont));
            c3.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase(
                    "Status: " + (o.getOrderStatus() != null ? o.getOrderStatus().toString() : ""), orderInfoFont));
            c4.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c4);

            document.add(headerTable);

            // Item Table
            PdfPTable itemTable = new PdfPTable(4);
            itemTable.setWidthPercentage(95);
            itemTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.setWidths(new int[] { 10, 2, 3, 4 });

            String[] headers = { "Product", "Qty", "Price", "Amount" };
            for (String h : headers) {
                PdfPCell hCell = new PdfPCell(new Phrase(h, headFont));
                hCell.setBackgroundColor(new BaseColor(52, 73, 94));
                hCell.setPadding(5);
                itemTable.addCell(hCell);
            }

            if (o.getCartItems() != null) {
                for (com.schoolapp.entity.SellCartItems item : o.getCartItems()) {
                    itemTable.addCell(new PdfPCell(new Phrase(item.getProductName(), cellFont)));
                    itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), cellFont)));
                    itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getPrice()), cellFont)));
                    itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getTotalAmount()), cellFont)));
                }
            }

            PdfPCell totalLabel = new PdfPCell(new Phrase("Order Total:", headFont));
            totalLabel.setBackgroundColor(new BaseColor(127, 140, 141));
            totalLabel.setColspan(3);
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase(
                    amountFormatter.format(o.getAmount() != null ? o.getAmount().doubleValue() : 0.0), headFont));
            totalValue.setBackgroundColor(new BaseColor(127, 140, 141));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.addCell(totalValue);

            document.add(itemTable);
            document.add(new Paragraph("\n"));
            grandTotal += (o.getAmount() != null ? o.getAmount().doubleValue() : 0.0);
        }

        Paragraph footer = new Paragraph(
                "\nTotal Records: " + data.size() + " | Grand Total: " + amountFormatter.format(grandTotal), titleFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    private byte[] generatePurchasePdf(List<PurchaseOrders> data, TransactionExportRequest request) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(39, 174, 96));
        Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Font orderInfoFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(21, 67, 96));
        Font headFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

        Paragraph title = new Paragraph("Purchase Transaction Detail Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        String dateRange = (request.getFromDate() != null && request.getToDate() != null)
                ? dateFormat.format(request.getFromDate()) + " - " + dateFormat.format(request.getToDate())
                : "All Time";
        Paragraph subTitle = new Paragraph("Date Range: " + dateRange, subTitleFont);
        subTitle.setAlignment(Element.ALIGN_CENTER);
        subTitle.setSpacingAfter(20);
        document.add(subTitle);

        double grandTotal = 0;
        for (PurchaseOrders o : data) {
            // Order Summary Bar
            PdfPTable headerTable = new PdfPTable(4);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(10);
            headerTable.setWidths(new int[] { 3, 5, 3, 3 });

            PdfPCell c1 = new PdfPCell(new Phrase("Order ID: " + o.getId(), orderInfoFont));
            c1.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c1);

            PdfPCell c2 = new PdfPCell(
                    new Phrase("Party: " + (o.getUser() != null ? o.getUser().getUsername() : "N/A"), orderInfoFont));
            c2.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(
                    new Phrase("Date: " + (o.getDate() != null ? o.getDate().format(ldtFormat) : ""), orderInfoFont));
            c3.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase(
                    "Status: " + (o.getOrderStatus() != null ? o.getOrderStatus().toString() : ""), orderInfoFont));
            c4.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(c4);

            document.add(headerTable);

            // Item Table
            PdfPTable itemTable = new PdfPTable(4);
            itemTable.setWidthPercentage(95);
            itemTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.setWidths(new int[] { 10, 2, 3, 4 });

            String[] headers = { "Product", "Qty", "Price", "Amount" };
            for (String h : headers) {
                PdfPCell hCell = new PdfPCell(new Phrase(h, headFont));
                hCell.setBackgroundColor(new BaseColor(46, 204, 113));
                hCell.setPadding(5);
                itemTable.addCell(hCell);
            }

            if (o.getCartItems() != null) {
                for (com.schoolapp.entity.PurchaseCartItems item : o.getCartItems()) {
                    itemTable.addCell(new PdfPCell(new Phrase(item.getProductName(), cellFont)));
                    itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), cellFont)));
                    itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getPrice()), cellFont)));
                    itemTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getTotalAmount()), cellFont)));
                }
            }

            PdfPCell totalLabel = new PdfPCell(new Phrase("Order Total:", headFont));
            totalLabel.setBackgroundColor(new BaseColor(127, 140, 141));
            totalLabel.setColspan(3);
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase(
                    amountFormatter.format(o.getAmount() != null ? o.getAmount().doubleValue() : 0.0), headFont));
            totalValue.setBackgroundColor(new BaseColor(127, 140, 141));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            itemTable.addCell(totalValue);

            document.add(itemTable);
            document.add(new Paragraph("\n"));
            grandTotal += (o.getAmount() != null ? o.getAmount().doubleValue() : 0.0);
        }

        Paragraph footer = new Paragraph(
                "\nTotal Records: " + data.size() + " | Grand Total: " + amountFormatter.format(grandTotal), titleFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }
}
