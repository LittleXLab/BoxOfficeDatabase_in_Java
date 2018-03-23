import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Method;

public class adminUI {
    private JTextField adminMTF;
    private JTextField adminDTF;
    private JTextField adminATF;
    private JComboBox adminYCB;
    private JComboBox adminCCB;
    private JButton SeaButton;
    private JTable adminSTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JPanel adminJP;
    private JScrollPane showSP;
    private JPanel deleRepoJP;
    private JPanel addEditJP;
    private JPanel dataJP;
    private JTextField mNameJTF;
    private JTextField classJTF;
    private JTextField dirJTF;
    private JTextField actJTF;
    private JTextField yearJTF;
    private JTextField boxJTF;
    private JTextField perJTF;
    private JTextField couJTF;
    private JLabel zhanweifu;
    private JButton reportButton;
    private JButton refreshButton;
    String seaSQL="select * from total where 电影名称 is not null";
    String deleSQL="delete from Class where 电影名称='";
    String addSQL1="insert into Class values('";
    String addSQL2="');insert into DirAct values('";
    String addSQL3="');insert into HistoryRank values('";
    String editClass="update Class set ";
    String editHR="update HistoryRank set ";
    String editDA="update DirAct set ";
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("adminUI");
        frame.setContentPane(new adminUI().adminJP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(300,100);
    }
    public adminUI(){
        adminYCB.addItem("全部");
        adminCCB.addItem("全部");
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
            adminCCB.addItem(SQL.temp);
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
            adminYCB.addItem(SQL.temp);
        }
        //下拉列表初始化
        zhanweifu.setPreferredSize(new Dimension(150,30));
        //mNameJTF.addFocusListener(new HintForJTF("电影名称", mNameJTF));
        SeaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql=seaSQL;
                if (!adminMTF.getText().isEmpty())
                    sql=seaSQL+" and 电影名称 like \'%"+adminMTF.getText()+"%\'";
                if (!adminDTF.getText().isEmpty())
                    sql=sql+" and 导演 like \'%"+adminDTF.getText()+"%\'";
                if (!adminATF.getText().isEmpty())
                    sql=sql+" and 主演 like \'%"+adminATF.getText()+"%\'";
                if (!adminYCB.getSelectedItem().equals("全部"))
                    sql=sql+" and 上映年份=\'"+adminYCB.getSelectedItem().toString()+"\'";
                if (!adminCCB.getSelectedItem().equals("全部"))
                    sql=sql+" and 类别=\'"+adminCCB.getSelectedItem().toString()+"\'";
                //System.out.println(sql);
                SQL.query(sql);
                try {
                    SQL.querynext();
                    updateData();
                    int i=0;
                    while (SQL.havenext&&i<20){
                        adminSTable.setValueAt(SQL.movie,i,0);
                        adminSTable.setValueAt(SQL.mclass,i,1);
                        adminSTable.setValueAt(SQL.dirctor,i,2);
                        adminSTable.setValueAt(SQL.actor,i,3);
                        adminSTable.setValueAt(SQL.year,i,4);
                        adminSTable.setValueAt(SQL.yearRank,i,5);
                        adminSTable.setValueAt(SQL.historyRank,i,6);
                        adminSTable.setValueAt(SQL.totalBox,i,7);
                        adminSTable.setValueAt(SQL.totalPeo,i,8);
                        adminSTable.setValueAt(SQL.totalCou,i,9);
                        SQL.querynext();
                        i++;
                    }
                    //System.out.print(i);
                    if (i==0)
                        JOptionPane.showMessageDialog(null,"输入数据有误","警告",JOptionPane.WARNING_MESSAGE);

                }catch (Exception ex){
                    System.err.println("Search Button\t"+ex.getMessage());
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //int i;
                String sql=null;
                //i=adminSTable.getSelectedRow();//返回数据减10得到行，列从0开始
                //System.out.println(adminSTable.getValueAt(i-10,0));
                sql=deleSQL+adminSTable.getValueAt(adminSTable.getSelectedRow(),0)+'\'';
                System.out.print(sql);
                SQL.excuteSQL(sql);
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    Method method=adminUI.class.getMethod("main", String[].class);
                    method.invoke(null,(Object)new String[]{"刷新"});
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });//怎么让原窗口关闭
        addButton.addActionListener(new ActionListener() {
            String movie=null;
            String totalBox=null;
            String totalPeo=null;
            String totalCou=null;
            String year=null;
            String dirctor=null;
            String actor=null;
            String mclass=null;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mNameJTF.getText().isEmpty()||yearJTF.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"电影名称、上映年份不能为空","警告消息",JOptionPane.WARNING_MESSAGE);
                }
                try{
                    if (mNameJTF.getText().toString().length()>30)
                        throw new java.io.IOException("电影名字过长");
                    if (classJTF.getText().toString().length()>8)
                        throw new java.io.IOException("类别过长");
                    if (dirJTF.getText().toString().length()>8)
                        throw new java.io.IOException("导演名字过长");
                    if (actJTF.getText().toString().length()>8)
                        throw new java.io.IOException("主演名字过长");
                    Integer.parseInt(yearJTF.getText());
                    if (!boxJTF.getText().isEmpty())
                    Float.parseFloat(boxJTF.getText());
                    if (!perJTF.getText().isEmpty())
                    Float.parseFloat(perJTF.getText());
                     if (!couJTF.getText().isEmpty())
                    Float.parseFloat(couJTF.getText());
                     movie=mNameJTF.getText().toString();
                     mclass=classJTF.getText().toString();
                     year=yearJTF.getText().toString();
                     dirctor=dirJTF.getText().toString();
                     actor=actJTF.getText().toString();
                     year=yearJTF.getText().toString();
                     totalBox=boxJTF.getText().toString();
                     totalPeo=perJTF.getText().toString();
                     totalCou=couJTF.getText().toString();
                     String sql=addSQL1+movie+"','"+mclass+addSQL2+movie+"','"+year+"','"+dirctor+"','"+actor+addSQL3+movie+"','"+totalBox+"','"+totalPeo+"','"+totalCou+"','"+year+"')";
                     System.out.println(sql);
                     SQL.excuteSQL(sql);
                     clear();
                }
                catch (IOException eio){
                    JOptionPane.showMessageDialog(null,eio,"提示信息",JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception e1){
                    System.out.print(e1);
                    JOptionPane.showMessageDialog(null,"上映年份、票房、人次、场次只能是数字","提示信息",JOptionPane.INFORMATION_MESSAGE);
                }
                clear();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movie=null;
                String year=null;
                String sql=null;
                boolean change=false;
                movie=adminSTable.getValueAt(adminSTable.getSelectedRow(),0).toString();
                year=adminSTable.getValueAt(adminSTable.getSelectedRow(),4).toString();
                try {
                    if (mNameJTF.getText().toString().length() > 30)
                        throw new java.io.IOException("电影名字过长");
                    if (classJTF.getText().toString().length() > 8)
                        throw new java.io.IOException("类别过长");
                    if (dirJTF.getText().toString().length() > 8)
                        throw new java.io.IOException("导演名字过长");
                    if (actJTF.getText().toString().length() > 8)
                        throw new java.io.IOException("主演名字过长");
                    if(!yearJTF.getText().isEmpty())
                        Integer.parseInt(yearJTF.getText());
                    if (!boxJTF.getText().isEmpty())
                        Float.parseFloat(boxJTF.getText());
                    if (!perJTF.getText().isEmpty())
                        Float.parseFloat(perJTF.getText());
                    if (!couJTF.getText().isEmpty())
                        Float.parseFloat(couJTF.getText());
                    if (!mNameJTF.getText().isEmpty()){
                        sql=editClass+"电影名称='"+mNameJTF.getText().toString()+"' where 电影名称='"+movie+"';";
                        movie=mNameJTF.getText().toString();
                        System.out.println(sql);
                        SQL.excuteSQL(sql);
                    }
                    if (!classJTF.getText().isEmpty()){
                        sql=editClass+"类别='"+classJTF.getText().toString()+"' where 电影名称='"+movie+"';";
                        System.out.println(sql);
                        SQL.excuteSQL(sql);
                    }
                    sql=editDA;
                    if (!dirJTF.getText().isEmpty()){
                        sql=sql+"导演='"+dirJTF.getText().toString();
                        change=true;
                    }
                    if (!actJTF.getText().isEmpty()){
                        sql=sql+"', 主演='"+actJTF.getText().toString();
                        change=true;
                    }
                    if (change){
                        sql=sql+"' where 电影名称='"+movie+"' and 上映年份='"+year+"';";
                        change=false;
                        System.out.println(sql);
                        SQL.excuteSQL(sql);
                    }
                    sql=editHR;
                    if (!boxJTF.getText().isEmpty()){
                        sql=sql+"总票房='"+boxJTF.getText().toString();
                        change=true;
                    }
                    if (!perJTF.getText().isEmpty()){
                        sql=sql+"', 总人次='"+perJTF.getText().toString();
                        change=true;
                    }
                    if (!couJTF.getText().isEmpty()){
                        sql=sql+"', 总场次='"+couJTF.getText().toString();
                        change=true;
                    }
                    if (change){
                        sql=sql+"' where 电影名称='"+movie+"';";
                        change=false;
                        System.out.println(sql);
                        SQL.excuteSQL(sql);
                    }
                    if (!yearJTF.getText().isEmpty()){
                        sql=editHR+"上映年份='"+yearJTF.getText().toString()+"' where 电影名称='"+movie+"' and 上映年份='"+year+"';";
                        System.out.println(sql);
                        SQL.excuteSQL(sql);
                        sql=editDA+"上映年份='"+yearJTF.getText().toString()+"' where 电影名称='"+movie+"' and 上映年份='"+year+"';";
                        System.out.println(sql);
                        SQL.excuteSQL(sql);
                    }
                }
                catch (IOException eio){
                    JOptionPane.showMessageDialog(null,eio,"提示信息",JOptionPane.INFORMATION_MESSAGE);
                }
                catch (Exception e1){
                    System.out.print(e1);
                    JOptionPane.showMessageDialog(null,"上映年份、票房、人次、场次只能是数字","提示信息",JOptionPane.INFORMATION_MESSAGE);
                }
                clear();
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        String[] col={"电影名称","类别","导演","主演","上映年份","年度排名","历史排名","总票房","总人次","总场次"};
        //            30char     8char  20char 20char int                               float   float   float
        String[][] content=new String[20][10];//20
        adminSTable=new JTable(content,col);
        adminSTable.setPreferredScrollableViewportSize(new Dimension(800,320));//320
        adminSTable.getColumnModel().getColumn(0).setPreferredWidth(160);
        adminSTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        adminSTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        adminSTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        adminSTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        adminSTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        adminSTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        adminSTable.getColumnModel().getColumn(7).setPreferredWidth(48);
        adminSTable.getColumnModel().getColumn(8).setPreferredWidth(60);
        adminSTable.getColumnModel().getColumn(9).setPreferredWidth(60);
        mNameJTF =new JTextField();
        classJTF =new JTextField();
        dirJTF =new JTextField();
        actJTF =new JTextField();
        yearJTF =new JTextField();
        boxJTF =new JTextField();
        perJTF =new JTextField();
        couJTF =new JTextField();
        mNameJTF.setPreferredSize(new Dimension(160,30));
        classJTF.setPreferredSize(new Dimension(60,30));
        dirJTF.setPreferredSize(new Dimension(110,30));
        actJTF.setPreferredSize(new Dimension(110,30));
        yearJTF.setPreferredSize(new Dimension(60,30));
        boxJTF.setPreferredSize(new Dimension(48,30));
        perJTF.setPreferredSize(new Dimension(60,30));
        couJTF.setPreferredSize(new Dimension(60,30));
    }
    void updateData(){
        for (int i=0;i<20;i++){
            adminSTable.setValueAt("",i,0);
            adminSTable.setValueAt("",i,1);
            adminSTable.setValueAt("",i,2);
            adminSTable.setValueAt("",i,3);
            adminSTable.setValueAt("",i,4);
            adminSTable.setValueAt("",i,5);
            adminSTable.setValueAt("",i,6);
            adminSTable.setValueAt("",i,7);
            adminSTable.setValueAt("",i,8);
            adminSTable.setValueAt("",i,9);
        }
    }//清空表格数据
    void clear(){
        mNameJTF.setText("");
        classJTF.setText("");
        dirJTF.setText("");
        actJTF.setText("");
        yearJTF.setText("");
        boxJTF.setText("");
        perJTF.setText("");
        couJTF.setText("");
    }
}
