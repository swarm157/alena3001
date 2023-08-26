package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class CellularAutomaton extends JPanel {
    private static int[][] grid;
    private int gridSize = 50;
    private int cellSize = 10;

    static boolean running = false;

    public CellularAutomaton() {
        grid = new int[gridSize][gridSize];
        setPreferredSize(new Dimension(gridSize * cellSize, gridSize * cellSize));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;
                grid[x][y] = 1 - grid[x][y];
                repaint();
            }
        });

        Timer timer = new Timer(100, e -> {
            if (running)
                updateGrid();
            repaint();
        });
        timer.start();
    }

    private void updateGrid() {
        int[][] newGrid = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int neighbors = countNeighbors(i, j);
                if (grid[i][j] == 1 && (neighbors < 2 || neighbors > 3)) {
                    newGrid[i][j] = 0;
                } else if (grid[i][j] == 0 && neighbors == 3) {
                    newGrid[i][j] = 1;
                } else {
                    newGrid[i][j] = grid[i][j];
                }
            }
        }
        grid = newGrid;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int neighborX = (x + i + gridSize) % gridSize;
                int neighborY = (y + j + gridSize) % gridSize;
                count += grid[neighborX][neighborY];
            }
        }
        count -= grid[x][y];
        return count;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 1) {
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cellular Automaton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(new CellularAutomaton());
        frame.setVisible(true);

        JFrame frame2 = new JFrame("buttons");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(200, 100);
        frame2.add(new JPanel(){{
            add(new JButton("change state"){{
                addActionListener(e -> {
                    if (running)
                        running = false;
                    else
                        running = true;
                });
            }});
            add(new JButton("save"){{
                addActionListener(e -> {
                    running =false;
                    saveToFile("save.save");
                });
            }});
            add(new JButton("load"){{
                addActionListener(e -> {
                    running =false;
                    grid = loadFromFile("save.save");
                });
            }});
        }});
        frame2.setVisible(true);
    }
    public static void saveToFile(String filename) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(grid);
            System.out.println("Состояние сетки успешно сохранено в файл " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении состояния сетки в файл " + filename + ": " + e.getMessage());
        }
    }

    public static int[][] loadFromFile(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            int[][] loadedGrid = (int[][]) objectInputStream.readObject();
            System.out.println("Состояние сетки успешно загружено из файла " + filename);
            return loadedGrid;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке состояния сетки из файла " + filename + ": " + e.getMessage());
            return null;
        }
    }
}