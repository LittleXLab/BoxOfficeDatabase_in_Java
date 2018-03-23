import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class Login {
    private JTextField userJTF;
    private JRadioButton AdmRadioButton;
    private JRadioButton VisRadioButton;
    private JPanel adminJP;
    private JPanel passJP;
    private JPanel userJP;
    private JButton loginBu;
    private JButton resetBu;
    private JPanel LoginJP;
    private JPasswordField passwordField1;
    final String username="admin";
    final String password="admin";
    public Login() {
        LoginJP.setSize(1024,1024);
        resetBu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        loginBu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    if (AdmRadioButton.isSelected()){
                        admLogin();
                        clear();
                    }else if (VisRadioButton.isSelected()){
                        visLogin();
                        clear();
                    }else
                        JOptionPane.showMessageDialog(null,"未选择角色","提示信息",JOptionPane.WARNING_MESSAGE);
            }
        });
    }//初始化、添加监听

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("登录");
        frame.setContentPane(new Login().LoginJP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(750,150);
    }

    public void visLogin(){     //游客登录
        if (userJTF.getText().isEmpty()||passwordField1.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"请输入用户名或密码","提示信息",JOptionPane.WARNING_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null,"登录成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
            SQL.ConnectSQL();
            callShow();
            //SQL.close();
        }
    }//游客登录 仅供查询
    public void admLogin(){
        if (username.equals(userJTF.getText())&&password.equals(passwordField1.getText())){
            JOptionPane.showMessageDialog(null,"登录成功","提示信息",JOptionPane.WARNING_MESSAGE);
            callAdminUI();
        }else if (userJTF.getText().isEmpty()||passwordField1.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"请输入用户名或密码","提示信息",JOptionPane.WARNING_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null,"用户名或者密码错误！\n请重新输入","提示信息",JOptionPane.WARNING_MESSAGE);
        }
    }//管理员登录 全部操作
    public void clear(){
        userJTF.setText("");
        passwordField1.setText("");
    }//清空文本框
    public void callShow(){
        try{
            Method method=show.class.getMethod("main", String[].class);
            method.invoke(null,(Object)new String[]{"查询窗口"});
        }catch (Exception e){
            e.printStackTrace();
        }
    }//呼出查询界面
    public void callAdminUI(){
        try {
            Method method=adminUI.class.getMethod("main", String[].class);
            method.invoke(null,(Object)new String[]{"管理窗口"});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
