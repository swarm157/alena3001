package org.example;

import javax.swing.JFrame;
import java.awt.*;


public class Main extends JFrame {
    {
        setSize(600, 600); // Установка размеров окна (ширина и высота)
        setTitle("Мое окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Установка операции закрытия по умолчанию
        setVisible(true);
    }
    public static void main(String[] args) {
        Main frame = new Main(); // Создание объекта JFrame с заголовком "Мое окно"


    }

    @Override
    public void paintComponents(Graphics g) {
     g.setColor(Color.red);
    }
}