import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL {
    static Connection connection=null;
    static ResultSet resultSet=null;
    static Statement statement=null;
    static String movie=null;
    static String totalBox=null;
    static String totalPeo=null;
    static String totalCou=null;
    static String year=null;
    static String dirctor=null;
    static String actor=null;
    static String mclass=null;
    static String yearRank=null;
    static String historyRank=null;
    static boolean havenext=true;
    static int count=0;
    static String temp=null;
    public static void main(String args[]){
        ConnectSQL();
        //report();
        //beginCount("select count(distinct 类别) as 类别数 from Class");
        //System.out.print(count+'\t');
    }
    public static void ConnectSQL(){
        try {
            String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver);
            System.out.println("注册成功");
            String URL="jdbc:sqlserver://localhost:1433;DatabaseName=BoxOffice;integratedSecurity=true";
            connection= DriverManager.getConnection(URL);
            System.out.println("连接成功");
        }catch (Exception e){
            System.err.println("connectSQL\t"+e.getMessage());
        }
    }//连接SQL
    public static void beginCount(String sql){
        try {
            Statement statement=connection.createStatement();
            resultSet=statement.executeQuery(sql);
            resultSet.next();
            count=resultSet.getInt(1);
        }catch (Exception e){
            System.err.println("query\t"+e.getMessage());
        }
    }//获取个数
    public static void afterCount(String sql){
        try{
            Statement statement=connection.createStatement();
            resultSet=statement.executeQuery(sql);
            //resultSet.next();
            //temp=resultSet.getString(1);
        }catch (Exception e){
            System.err.println("query\t"+e.getMessage());
        }
    }//获取下拉框内容
    public static void excuteSQL(String sql){
        try {
            Statement statement=connection.createStatement();
            statement.execute(sql);
        }catch (Exception e){
            System.err.println("excute\t"+e.getMessage());
        }
    }
    public static void query(String sql){
        try {
            Statement statement=connection.createStatement();
            //String sql="select   from HistoryRank";
            resultSet=statement.executeQuery(sql);
            havenext=true;
        }catch (Exception e){
            System.err.println("query\t"+e.getMessage());
        }
    }//查询
    public static void querynext(){
        try {
            if (resultSet.next()){
                movie=resultSet.getString("电影名称");
                totalBox=resultSet.getString("总票房");
                totalPeo=resultSet.getString("总人次");
                totalCou=resultSet.getString("总场次");
                year=resultSet.getString("上映年份");
                dirctor=resultSet.getString("导演");
                actor=resultSet.getString("主演");
                mclass=resultSet.getString("类别");
                yearRank=resultSet.getString("年度排名");
                historyRank=resultSet.getString("历史排名");
            }else havenext=false;

        }catch (Exception e){
            System.err.println("query next"+e);
        }
    }//配套查询窗口，查询下一条记录

    public static TableModel report(){
        //String[] columnNames = {"电影名称","类别","导演","主演","上映年份","年度排名","历史排名","总票房","总人次","总场次"};
        String reportYear=" ";
        String reportClass=" ";
        String[] columnNames = {"上映年份","类别","电影名称","导演/平均票房","总票房"};   //列名
        //int i=5;
        //String[] columnNames=new String[i];
        String [][]tableVales=new String[0][5]; //数据
        DefaultTableModel defaultTableModel=new DefaultTableModel(tableVales,columnNames);
        String[] rowValues={"DF","DS","DF","dd","fds"};
        //defaultTableModel.addRow(rowValues);
        int i=0;
        try {
            Statement statement=connection.createStatement();
            String sqlMain="select 上映年份,类别,电影名称,导演,总票房 from total order by 上映年份 asc,类别 asc,总票房 asc";
            //String sqlVice="select 上映年份,AVG(总票房) from total group by 上映年份 order by 上映年份 asc";
            ResultSet resultSetMain=statement.executeQuery(sqlMain);
            //ResultSet resultSetVice=statement.executeQuery(sqlVice);

            while (resultSetMain.next()){
                if (!reportYear.equals(resultSetMain.getString(1))){
                    reportYear=resultSetMain.getString(1).toString();
                    //System.out.println(reportYear);
                    i++;
                    rowValues=new String[]{reportYear," ","平均票房"," "," "};
                    defaultTableModel.addRow(rowValues);
                }
                if (!reportClass.equals(resultSetMain.getString(2))){
                    reportClass=resultSetMain.getString(2).toString();
                    //System.out.println(reportClass);
                    i++;
                    rowValues=new String[]{" ",resultSetMain.getString(2).toString()," "," "," "};
                    defaultTableModel.addRow(rowValues);
                }
                //System.out.println(resultSetMain.getString(3)+resultSetMain.getString(4)+resultSetMain.getString(5));
                i++;
                rowValues=new String[]{" "," ",resultSetMain.getString(3),resultSetMain.getString(4),resultSetMain.getString(5)};
                defaultTableModel.addRow(rowValues);
            }
        }catch (Exception e){
            System.err.println("reportFirst\t"+e.getMessage());
        }
        try{
            Statement statement=connection.createStatement();
            String sqlVice="select 上映年份,AVG(总票房) from total group by 上映年份 order by 上映年份 asc";
            ResultSet resultSetVice=statement.executeQuery(sqlVice);
            resultSetVice.next();
            for (int j=0;j<i;j++){
                if (((TableModel)defaultTableModel).getValueAt(j,0).equals(resultSetVice.getString(1))){
                    ((TableModel)defaultTableModel).setValueAt(resultSetVice.getString(2).toString(),j,3);
                    resultSetVice.next();
                }
            }
        }catch (Exception e){
            System.err.println("reportAgain\t"+e.getMessage());
        }
        TableModel tableModel = defaultTableModel;
        for (int j=0;j<i;j++){
            //System.out.println(tableModel.getColumnName(0));
            System.out.println(tableModel.getValueAt(j,0).toString()+tableModel.getValueAt(j,1).toString()+tableModel.getValueAt(j,2).toString()+tableModel.getValueAt(j,3).toString()+tableModel.getValueAt(j,4).toString());
        }
        return new JTable((TableModel)defaultTableModel).getModel();
    }

    public static void close(){
        try {
            System.out.println("haha0");
            resultSet.close();
            System.out.println("haha1");
            statement.close();
            connection.close();
        }catch (Exception e){
            System.err.println("close\t"+e.getMessage());
        }
    }//断开SQL
}
