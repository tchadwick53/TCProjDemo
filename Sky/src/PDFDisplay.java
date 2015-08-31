import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PDFDisplay {
  private static String FILE = "PDFDisplay.pdf";
  private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
      Font.BOLD);
  private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.NORMAL);

  public static void main(String[] args) {
    try {
      jsonImport.main();
      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(FILE));
      document.open();
      addMetaData(document);
      addTitlePage(document);
      addContent(document);
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // iText allows to add metadata to the PDF which can be viewed in your Adobe
  // Reader
  // under File -> Properties
  private static void addMetaData(Document document) {
    document.addTitle("Bill");
    document.addSubject("Sky Demo Bill");
  }

private static void addTitlePage(Document document)
      throws DocumentException {
    Paragraph Statement = new Paragraph();
    // We add one empty line
    addEmptyLine(Statement, 1);
    // Lets write a big header
    Statement.add(new Paragraph("Sky Bill", catFont));

    addEmptyLine(Statement, 1);
    
    document.add(addStatementDetails(Statement) );
    
    Paragraph subs = new Paragraph();
    subs.add(new Paragraph("You Subs", catFont));
    document.add(addsubs(subs));
  }

  
  public static Paragraph addStatementDetails(Paragraph preface)
  {
	  for(Object obj : jsonImport.statementArray)
	  {
	  	JSONObject field = (JSONObject) obj;
	      if((Object) field.get("generated") !=null)
	      	preface.add(new Paragraph("This bill was generated on: " + (String)field.get("generated")) );
	      		
	      if((Object) field.get("due") !=null)
	      	preface.add(new Paragraph("Your bill is due to be payed on: " + (String)field.get("due")) );
	      
	  	if((Object) field.get("period") != null)
	  	{
	  		for(Object objInner : jsonImport.periodArray)
	  		{
	  			JSONObject fieldInner = (JSONObject) objInner;
	  			System.out.println(fieldInner);
	  			if( (Object) fieldInner.get("from") != null )
	  				preface.add( new Paragraph("This bill starts from: " + (String)fieldInner.get("from")) );
	  			if((Object) fieldInner.get("to") != null )
	  				preface.add( new Paragraph("And covers till " + (String)fieldInner.get("to")) );
	  		}
	  	}
	  }
	  return preface;
  }
  
  public static Paragraph addsubs (Paragraph preface)
  {
	  preface.add(new Paragraph("You are subcripbed to the following items ", subFont));
	  createListSubs(preface, "subs");
	  return preface;
  }
  

private static void addContent(Document document) throws DocumentException, FileNotFoundException, IOException, ParseException {
    Anchor anchor = new Anchor();
    anchor.setName("bill");

    // Second parameter is the number of t         m                                                                                                            hhe chapter
    Paragraph subPara = new Paragraph(anchor);
    
    Chapter catPart = new Chapter(subPara, 0);
    catPart.setNumberDepth(0);
    
    Section subCatPart = catPart.addSection(subPara);
    subCatPart.add(new Paragraph("Sky bill ", catFont));

    // add a list
    Paragraph paragraph = new Paragraph();
    addEmptyLine(paragraph, 0);
    subCatPart.add(paragraph);

    // add a table
    createTable(subCatPart);
    
    addEmptyLine(paragraph, 2);
    createListFilms(subCatPart);
    subCatPart.add(paragraph);
    
    Object billTotal = jsonImport.billTotal; 
    subCatPart.add(new Paragraph("The total for this bill is: " + billTotal.toString()));
    
    // now add all this to the document
    document.add(catPart);
  }

  private static void createTable(Section subCatPart)
      throws BadElementException, FileNotFoundException, IOException, ParseException {
    PdfPTable table = new PdfPTable(3);

    PdfPCell c1 = new PdfPCell(new Phrase("Called"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);

    c1 = new PdfPCell(new Phrase("Duration"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);

    c1 = new PdfPCell(new Phrase("Cost"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);
    table.setHeaderRows(1);
    
    for(Object obj : jsonImport.callsMadeArray)
    {
    	JSONObject field = (JSONObject) obj;
    	if((Object) field.get("called") != null)
    	{
    		Object called = (Object) field.get("called");
    		table.addCell( (String)called );
    	}
        if((Object) field.get("duration") !=null)
        {
        	Object duration = (Object) field.get("duration");
        	table.addCell( (String)duration );
        }
        		
        if((Object) field.get("cost") !=null)
        {
        	Object cost = (Object) field.get("cost");
        	table.addCell( cost.toString() );
        }
    }
    table.addCell("");
    table.addCell("");
    Object callChargeTotal = jsonImport.callsMadeTotal;
    table.addCell("Total - "+ callChargeTotal.toString() );
    subCatPart.add(table);

  }
  
  private static void createListFilms(Section subCatPart) 
  {
	  subCatPart.add(new Paragraph("Films you rented this month;"));
	  	for(Object obj : jsonImport.storeRentArray)
	    {
	    	JSONObject field = (JSONObject) obj; 
	    	subCatPart.add(new Paragraph( (String)field.get("title") + ", £" + field.get("cost").toString()  ));
	    }
	  	subCatPart.add(new Paragraph("Films you bought this month;"));
  		for(Object obj : jsonImport.storeBoughtArray)
	    {
	    	JSONObject field = (JSONObject) obj; 
	    	subCatPart.add(new Paragraph( (String)field.get("title") + ", £" + field.get("cost").toString()  ));
	    }
  }

  private static void createListSubs(Paragraph preface, String area) 
  {
    if(area.equals("subs"))
    {
	    for(Object obj : jsonImport.subscriptionsArray)
	    {
	    	JSONObject field = (JSONObject) obj;	
	    	preface.add(new Paragraph( (String)field.get("name") + ", " + (String)field.get("type") + ", £" + field.get("cost").toString()  ));
	    }
    }
  }

  private static void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }
} 