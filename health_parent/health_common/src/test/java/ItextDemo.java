import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @Auther lxy
 * @Date
 */
public class ItextDemo {
    @Test
    public void test(){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("D:\\test.pdf"));
            document.open();
            document.add(new Paragraph("hello itext"));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void printPDF(){
        try {
            //创建一个PDF文件
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream("D:\\test.pdf"));
            //打开文档
            document.open();
            //填充数据
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            document.add(new Paragraph("你好", new Font(bfChinese)));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
