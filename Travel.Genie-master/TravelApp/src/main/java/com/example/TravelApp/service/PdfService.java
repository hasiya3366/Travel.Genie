package com.example.TravelApp.service; // 🎯 'TravelApp' කෑල්ල ටී සහ ඒ කැපිටල් කරන්න!

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {

    public ByteArrayInputStream generateBookingReceipt(Map<String, Object> data) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font priceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new BaseColor(15, 118, 110));

            Paragraph title = new Paragraph("TRAVELGenie - INVOICE BREAKDOWN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Customer Email: " + data.get("email"), normalFont));
            document.add(new Paragraph("Travel Date: " + data.get("date"), normalFont));
            document.add(new Paragraph("Total Travelers: " + data.get("travelers"), normalFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("--- Booking Customization Details ---", headerFont));
            document.add(new Paragraph("Package Name: " + data.get("packageName"), normalFont));
            document.add(new Paragraph("Hotel Option: " + data.get("hotelName"), normalFont));
            document.add(new Paragraph("Selected Vehicle: " + data.get("vehicle"), normalFont));
            document.add(Chunk.NEWLINE);

            Paragraph total = new Paragraph("Total Paid: LKR " + data.get("totalPrice"), priceFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
