import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//增加票房筛选
public class show {
    private JTextField movnamJTF;
    private JTextField dirctorJTF;
    private JTextField actorJTF;
    private JComboBox yearCB;
    private JButton seaButton;
    private JComboBox classCB;
    private JPanel showPanel;
    private JTable showTable;
    private JButton reportButton;
    //String basicSQL="select * from Class,DirAct,HistoryRank,YearRank where Class.电影名称=DirAct.电影名称 and Class.电影名称=HistoryRank.电影名称 and HistoryRank.总票房=YearRank.总票房 and HistoryRank.上映年份=YearRank.上映年份 and HistoryRank.上映年份=DirAct.上映年份";
    String basicSQL="select * from total where 电影名称 is not null";
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("查询");
        frame.setContentPane(new show().showPanel);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(300,100);
    }
    public show(){
        yearCB.addItem("全部");
        classCB.addItem("全部");
        /*
        yearCB.addItem("2010");
        yearCB.addItem("2011");
        yearCB.addItem("2012");
        yearCB.addItem("2013");
        yearCB.addItem("2014");
        yearCB.addItem("2015");
        yearCB.addItem("2016");
        yearCB.addItem("2017");
        classCB.addItem("喜剧");
        classCB.addItem("奇幻");
        classCB.addItem("魔幻");
        classCB.addItem("冒险");
        classCB.addItem("科幻");
        classCB.addItem("剧情");
        classCB.addItem("警匪");
        classCB.addItem("惊悚");
        classCB.addItem("古装");
        classCB.addItem("动作");
        classCB.addItem("动画");*/
        SQL.ConnectSQL();
        SQL.beginCount("select count(distinct 类别) from Class");
        SQL.afterCount("select distinct 类别 from Class");
        for (int i=SQL.count;i>0;i--){
            try {
                SQL.resultSet.next();
                SQL.temp=SQL.resultSet.getString(1);
            }catch (Exception e){
                System.err.println("query\t"+e.getMessage());
            }
            classCB.addItem(SQL.temp);
        }
        SQL.beginCount("select count(distinct 上映年份) from HistoryRank");
        SQL.afterCount("select distinct 上映年份 from HistoryRank");
        for (int i=SQL.count;i>0;i--){
            try {
                SQL.resultSet.next();
                SQL.temp=SQL.resultSet.getString(1);
            }catch (Exception e){
                System.err.println("query\t"+e.getMessage());
            }
            yearCB.addItem(SQL.temp);
        }
        seaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sql=basicSQL;
                if (!movnamJTF.getText().isEmpty())
                    sql=basicSQL+" and 电影名称 like \'%"+movnamJTF.getText()+"%\'";
                if (!dirctorJTF.getText().isEmpty())
                    sql=sql+" and 导演 like \'%"+dirctorJTF.getText()+"%\'";
                if (!actorJTF.getText().isEmpty())
                    sql=sql+" and 主演 like \'%"+actorJTF.getText()+"%\'";
                if (!yearCB.getSelectedItem().equals("全部"))
                    sql=sql+" and 上映年份=\'"+yearCB.getSelectedItem().toString()+"\'";
                if (!classCB.getSelectedItem().equals("全部"))
                    sql=sql+" and 类别=\'"+classCB.getSelectedItem().toString()+"\'";
                //System.out.println(sql);
                SQL.query(sql);
                try {
                    SQL.querynext();
                    updateData();
                    int i=0;
                    while (SQL.havenext&&i<40){
                        showTable.setValueAt(SQL.movie,i,0);
                        showTable.setValueAt(SQL.mclass,i,1);
                        showTable.setValueAt(SQL.dirctor,i,2);
                        showTable.setValueAt(SQL.actor,i,3);
                        showTable.setValueAt(SQL.year,i,4);
                        showTable.setValueAt(SQL.yearRank,i,5);
                        showTable.setValueAt(SQL.historyRank,i,6);
                        showTable.setValueAt(SQL.totalBox,i,7);
                        showTable.setValueAt(SQL.totalPeo,i,8);
                        showTable.setValueAt(SQL.totalCou,i,9);
                        SQL.querynext();
                        i++;
                    }
                    //System.out.print(i);
                    if (i==0)
                        JOptionPane.showMessageDialog(null,"输入数据有误","警告",JOptionPane.WARNING_MESSAGE);

                }catch (Exception ex){
                    System.err.println("Search Button\t"+ex.getMessage());
                }
                //showTable.setValueAt(SQL.statement,1,0);
                //SQL.close();
            }
        });
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*final Object[] columnNames = {"姓名","学号"};
                Object[][] rowData = {
                        {"小明","2015110512"},
                        {"小","2015110"},
                        {"小红","2015110511"}
                };
                JTable jtable=new JTable(rowData, columnNames);*/
                /*String[] columnNames = {"A","B"};   //列名
                String [][]tableVales={{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}}; //数据
                TableModel tableModel = new DefaultTableModel(tableVales,columnNames);
                JTable table = new JTable(tableModel);*/
                new PrintTable(SQL.report()).PreviewTable();

            }
        });
    }//下拉框初始化 查询监听

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //showTable=new JTable(40,10);
        String[] col={"电影名称","类别","导演","主演","上映年份","年度排名","历史排名","总票房","总人次","总场次"};
        String[][] content=new String[40][10];
        //DefaultTableModel model=new DefaultTableModel(content,col);
        showTable=new JTable(content,col);
        showTable.setPreferredScrollableViewportSize(new Dimension(800,650));
        //TableColumn tableColumn=showTable.getColumnModel().getColumn(0);
        //showTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        showTable.getColumnModel().getColumn(0).setPreferredWidth(160);
        showTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        showTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        showTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        showTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        showTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        showTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        showTable.getColumnModel().getColumn(7).setPreferredWidth(48);
        showTable.getColumnModel().getColumn(8).setPreferredWidth(60);
        showTable.getColumnModel().getColumn(9).setPreferredWidth(60);
    }//输出结果表格初始化
    void updateData(){
        for (int i=0;i<40;i++){
            showTable.setValueAt("",i,0);
            showTable.setValueAt("",i,1);
            showTable.setValueAt("",i,2);
            showTable.setValueAt("",i,3);
            showTable.setValueAt("",i,4);
            showTable.setValueAt("",i,5);
            showTable.setValueAt("",i,6);
            showTable.setValueAt("",i,7);
            showTable.setValueAt("",i,8);
            showTable.setValueAt("",i,9);
        }
    }//清空表格数据
}
