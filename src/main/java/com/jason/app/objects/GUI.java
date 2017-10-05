package com.jason.app.objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUI {
    public void getGUI () {
        JFrame jf=new JFrame("Salary Calculator");
        jf.setLayout(null);

        JPanel jpanel= new JPanel();
        jpanel.setLayout(null);
        jpanel.setBackground(Color.WHITE);
        jpanel.setBounds(50,0,450,250);

        Container c=jf.getContentPane();
        c.add(jpanel,BorderLayout.CENTER);

        //label here
        JLabel fromLabel, toLabel, nameLabel;
        fromLabel = new JLabel("开始日期:");
        toLabel = new JLabel("结束日期:");
        nameLabel = new JLabel("查询者姓名:");
        fromLabel.setBounds(10,65,70,30);
        toLabel.setBounds(10,100,70,30);
        nameLabel.setBounds(10,135,70,30);
        fromLabel.setBackground(Color.GRAY);
        toLabel.setBackground(Color.GRAY);
        nameLabel.setBackground(Color.GRAY);

        jpanel.add(fromLabel);
        jpanel.add(toLabel);
        jpanel.add(nameLabel);

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] array = format.format(date).toString().split("-");

        //text filed here
        JTextField fromText, toText, nameText;
        fromText = new JTextField(array[0]+"-"+array[1]+"-");
        toText = new JTextField(array[0]+"-"+array[1]+"-");
        nameText = new JTextField();
        fromText.setBounds(90,65,150,30);
        toText.setBounds(90,100,150,30);
        nameText.setBounds(90,135,150,30);

        jpanel.add(fromText);
        jpanel.add(toText);
        jpanel.add(nameText);

        //button here
        JButton find, search;
        find = new JButton("加载表格");
        search = new JButton("查询工资");

        find.addActionListener(new ActionListener() {
            String string = "";
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fromText.getText().isEmpty() && !toText.getText().isEmpty()) {
                    string = String.format("Loading Data from %s to %s.",fromText.getText().toString(),toText.getText().toString());
                }else{
                    string = "Date is wrong.";
                }
                JOptionPane.showMessageDialog(null, string);
            }
        });

        search.addActionListener(new ActionListener() {
            String s = "";
            @Override
            public void actionPerformed (ActionEvent event) {
                if (!nameText.getText().isEmpty()) {
                    s = "Finding Salary For: "+nameText.getText();

                }else if (nameText.getText().equals("")) {
                    s = "No Name to search";
                }
                JOptionPane.showMessageDialog(null, s);
            }
        });

        find.setBackground(Color.pink);
        find.setOpaque(true);
        find.setBounds(5,170,100,40);
        search.setBackground(Color.pink);
        search.setOpaque(true);
        search.setBounds(120,170,115,40);

        jpanel.add(find);
        jpanel.add(search);

        //final setting
        jf.setBounds(0,0,500,250);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
