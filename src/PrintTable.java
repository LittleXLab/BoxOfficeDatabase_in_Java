import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.*;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.PageOfPagesFunction;
import org.jfree.report.modules.gui.base.PreviewDialog;

import java.awt.geom.*;
public class PrintTable {
    JFreeReport report;

    public PrintTable(TableModel data){
        report = new JFreeReport();    //构造JFreeReport对象
        SetTableStyle(report,data);
        //把数据放进表格内
        report.setData(data);
    }

    //预览报表
    public void PreviewTable(){
        JFreeReportBoot jfrb = JFreeReportBoot.getInstance();
        jfrb.start();
        PreviewDialog preview = null;    //预览窗口对象
        try {
            // 将生成的报表放到预览窗口中
            preview = new PreviewDialog(report);
        } catch (ReportProcessingException e) {
            e.printStackTrace();
        }

        if (preview != null) {
            preview.pack();
            preview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // 设置报表起始点及长宽
            preview.setLocation(new Point(100, 100));
            preview.setSize(new Dimension(800, 600));

            // 显示报表预览窗口
            preview.setVisible(true);
            preview.requestFocus();
        }
    }

    void SetTableStyle(JFreeReport report,TableModel data){
        int ROW_HEIGHT = 30;    //报告：行高度
        int HEADER_HEIGHT = 40;    //报告：报头高度
        report.setName("票房报表");
        PageDefinition pd = report.getPageDefinition();    //取得报告页面定义
        float pageWidth = pd.getWidth();    //取得打印材质的页宽
        System.out.println(pageWidth);
        //System.out.println(pageWidth);
        //System.out.println(pageWidth);
        //System.out.println("哇卡卡卡");
        //System.out.println(pageWidth);
        //System.out.println(pageWidth);

        //定义页头
        PageHeader header = new PageHeader();
        LabelElementFactory title = new LabelElementFactory();
        //标题元素
        title.setText("票房报表");    //设置文本内容
        title.setColor(Color.BLACK);    //设置颜色
        title.setFontSize(new Integer(27));
        title.setAbsolutePosition(new Point2D.Float(0, HEADER_HEIGHT));    //设置显示位置
        title.setMinimumSize(new Dimension((int) pageWidth, 36));    //设置尺寸
        title.setHorizontalAlignment(ElementAlignment.MIDDLE);
        title.setVerticalAlignment(ElementAlignment.MIDDLE);
        title.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(title.createElement());
        report.setPageHeader(header);
        int []a={0,140,200,320,390};
        int []b={140,60,120,70,60};
        if (data != null && data.getColumnCount() > 0) {
            report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));    //绘制表格的横线
            //定义报表头
            ReportHeader reportHeader = new ReportHeader();
            for (int i = 0; i < data.getColumnCount(); i++) {
                //字段名元素
                LabelElementFactory col = new LabelElementFactory();
                col.setName(data.getColumnName(i));
                col.setColor(Color.BLACK);
                col.setHorizontalAlignment(ElementAlignment.CENTER);
                col.setVerticalAlignment(ElementAlignment.MIDDLE);
                col.setDynamicHeight(true);

                col.setAbsolutePosition(new Point2D.Float(a[i], 0));
                col.setMinimumSize(new Dimension(b[i], ROW_HEIGHT));    //设置最小尺寸
                col.setBold(true);    //设置是否粗体显示
                col.setText(data.getColumnName(i));
                reportHeader.addElement(col.createElement());
                reportHeader.addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), col.getAbsolutePosition().getX()));    //元素左侧竖线
                reportHeader.addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));    //元素上方横线
                reportHeader.addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), pageWidth));    //元素右侧竖线
                report.setReportHeader(reportHeader);

                //字段内容元素
                TextFieldElementFactory dataText = new TextFieldElementFactory();
                dataText.setName(data.getColumnName(i));
                //System.err.println(data.getColumnName(i));
                dataText.setColor(Color.BLACK);
                dataText.setAbsolutePosition(new Point2D.Float(a[i], 0));
                dataText.setMinimumSize(new Dimension(b[i], ROW_HEIGHT));
                dataText.setHorizontalAlignment(ElementAlignment.CENTER);

                dataText.setVerticalAlignment(ElementAlignment.MIDDLE);
                dataText.setDynamicHeight(true);
                dataText.setWrapText(new Boolean(true));
                dataText.setNullString("-");    //如果字段内容为空，显示的文本

                dataText.setFieldname(data.getColumnName(i));
                report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
                report.getItemBand().addElement(dataText.createElement());
                report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), -100));
                report.getItemBand().addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), dataText.getAbsolutePosition().getX()));
                report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
            }// end for(int i=0;i<columnNames.length;i++)

            //最后的竖线
            report.getItemBand().addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), pageWidth));

            PageFooter footer = new PageFooter();

            //设置页号
            PageOfPagesFunction pageFunction = new PageOfPagesFunction("PAGE_NUMBER");    //构造一个页号函数对象
            pageFunction.setFormat("{0} / {1}页");    //设置页号显示格式（此处显示的格式为“1/5页”）
            report.addExpression(pageFunction);

            TextFieldElementFactory pageCount = new TextFieldElementFactory();
            pageCount.setFieldname("PAGE_NUMBER");
            pageCount.setColor(Color.BLACK);
            pageCount.setAbsolutePosition(new Point2D.Float(0, 0));
            pageCount.setMinimumSize(new Dimension((int) pageWidth, 0));
            pageCount.setHorizontalAlignment(ElementAlignment.RIGHT);
            pageCount.setVerticalAlignment(ElementAlignment.MIDDLE);
            pageCount.setDynamicHeight(true);
            footer.addElement(pageCount.createElement());
            report.setPageFooter(footer);

            ReportFooter reportFooter = new ReportFooter();
            reportFooter.addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
            report.setReportFooter(reportFooter);
        }
    }
    public static void main(String []args){
        System.out.println("Testing...");
        /*final String[] columnNames = {"姓名","学号"," "," "," "};
        String[][] rowData = {
                {"小明","2015110512"},
                {"小","2015110"},
                {"小红","2015110511"}
        };
        //JTable jtable=new JTable(rowData, columnNames);
        DefaultTableModel tableModel=new DefaultTableModel(rowData,columnNames);
        JTable jtable=new JTable(tableModel);
        new PrintTable(jtable.getModel()).PreviewTable();*/
        //jtable=new JTable(SQL.report());
        //new PrintTable(jtable.getModel()).PreviewTable();
        //new PrintTable(new TableModel()).PreviewTable();
       //
        //SQL.ConnectSQL();
        //new PrintTable(SQL.report()).PreviewTable();
        /*for (int j=0;j<9;j++){
            //System.out.println(tableModel.getColumnName(0));
            System.out.println(SQL.report().getValueAt(j,0).toString()+SQL.report().getValueAt(j,1).toString()+SQL.report().getValueAt(j,2).toString()+SQL.report().getValueAt(j,3).toString()+SQL.report().getValueAt(j,4).toString());
        }*/
        /*for (int i=0;i<3;i++){
            System.out.println(jtable.getModel().getValueAt(i,0).toString()+jtable.getModel().getValueAt(i,1).toString());
        }*/
    }
}