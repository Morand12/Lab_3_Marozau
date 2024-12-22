//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem saveToCSVMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem searchRangeMenuItem;
    private JMenuItem showHelpMenuItem;
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;

    protected void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));

            for (int i = 0; i < this.data.getRowCount(); ++i) {
                out.writeDouble((Double) this.data.getValueAt(i, 0));
                out.writeDouble((Double) this.data.getValueAt(i, 1));
            }

            out.close();
        } catch (Exception var4) {
        }

    }

    protected void saveToTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);
            out.println("Результаты табулирования многочлена по схемеГорнера");
            out.print("Многочлен: ");

            String var10001;
            int i;
            for (i = 0; i < this.coefficients.length; ++i) {
                var10001 = String.valueOf(this.coefficients[i]);
                out.print(var10001 + "*X^" + (this.coefficients.length - i - 1));
                if (i != this.coefficients.length - 1) {
                    out.print(" + ");
                }
            }

            out.println("");
            var10001 = String.valueOf(this.data.getFrom());
            out.println("Интервал от " + var10001 + " до " + String.valueOf(this.data.getTo()) + " с шагом " + String.valueOf(this.data.getStep()));
            out.println("====================================================");

            for (i = 0; i < this.data.getRowCount(); ++i) {
                var10001 = String.valueOf(this.data.getValueAt(i, 0));
                out.println("Значение в точке " + var10001 + " равно " + String.valueOf(this.data.getValueAt(i, 1)));
            }

            out.close();
        } catch (FileNotFoundException var4) {
        }

    }

    protected void saveToCSVFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);

            int i;
            for (i = 0; i < this.data.getColumnCount(); ++i) {
                out.print(this.data.getColumnName(i));
                out.print(",");
            }

            out.println();

            for (i = 0; i < this.data.getRowCount(); ++i) {
                for (int j = 0; j < this.data.getColumnCount(); ++j) {
                    out.print(this.data.getValueAt(i, j));
                    out.print(",");
                }

                out.println();
            }

            out.close();
        } catch (FileNotFoundException var5) {
        }

    }

    private Action createSaveAction(String name, final int type) {
        Action saveAction = new AbstractAction(name) {
            public void actionPerformed(ActionEvent event) {
                if (MainFrame.this.fileChooser == null) {
                    MainFrame.this.fileChooser = new JFileChooser();
                    MainFrame.this.fileChooser.setCurrentDirectory(new File("."));
                }

                if (MainFrame.this.fileChooser.showSaveDialog(MainFrame.this) == 0) {
                    File f = MainFrame.this.fileChooser.getSelectedFile();
                    String filePath = f.getPath();
                    switch (type) {
                        case 0:
                        default:
                            f = new File(filePath + ".txt");
                            MainFrame.this.saveToTextFile(f);
                            break;
                        case 1:
                            f = new File(filePath + ".bin");
                            MainFrame.this.saveToGraphicsFile(f);
                            break;
                        case 2:
                            f = new File(filePath + ".csv");
                            MainFrame.this.saveToCSVFile(f);
                    }
                }

            }
        };
        return saveAction;
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        JMenu saveMenu = new JMenu("Сохранить");
        fileMenu.add(saveMenu);
        JMenu helpMenu = new JMenu("Справка");
        menuBar.add(helpMenu);
        this.saveToTextMenuItem = saveMenu.add(this.createSaveAction("Сохранить в текстовый файл", 0));
        this.saveToTextMenuItem.setEnabled(false);
        this.saveToGraphicsMenuItem = saveMenu.add(this.createSaveAction("Сохранить данные для построения графика", 1));
        this.saveToGraphicsMenuItem.setEnabled(false);
        this.saveToCSVMenuItem = saveMenu.add(this.createSaveAction("Сохранить в CSV файл", 2));
        this.saveToCSVMenuItem.setEnabled(false);
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", 3);
                MainFrame.this.renderer.setNeedle(value);
                MainFrame.this.getContentPane().repaint();
            }
        };
        Action searchRangeAction = new AbstractAction("Найти из диапазона") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите начало диапазона", "Поиск значения", 3);
                MainFrame.this.renderer.setRangeStart(value);
                value = JOptionPane.showInputDialog(MainFrame.this, "Введите конец диапазона", "Поиск значения", 3);
                MainFrame.this.renderer.setRangeEnd(value);
                MainFrame.this.getContentPane().repaint();
            }
        };
        this.searchValueMenuItem = tableMenu.add(searchValueAction);
        this.searchRangeMenuItem = tableMenu.add(searchRangeAction);
        this.searchValueMenuItem.setEnabled(false);
        this.searchRangeMenuItem.setEnabled(false);
        Action showHelpAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent ev) {
                ImageIcon icon = new ImageIcon("C:/Users/Morand/IdeaProjects/LaB_3_Marozau/src/StudentPhoto.jpg");
                JOptionPane.showConfirmDialog(MainFrame.this, "Морозов 9 группа", "wat", 0, -1, icon);
            }
        };
        this.showHelpMenuItem = helpMenu.add(showHelpAction);
        this.showHelpMenuItem.setEnabled(true);
    }

    private void initButtons() {
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(MainFrame.this.textFieldFrom.getText());
                    Double to = Double.parseDouble(MainFrame.this.textFieldTo.getText());
                    Double step = Double.parseDouble(MainFrame.this.textFieldStep.getText());
                    to = ((to / step) - (to / step) % 1) * step;
                    if (step > Math.abs(to - from) || step <= 0 || from >= to) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Ошибка: введены неверные данные", "Ошибочный формат числа", 2);

                    } else {
                        MainFrame.this.data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                        JTable table = new JTable(MainFrame.this.data);
                        table.setDefaultRenderer(Double.class, MainFrame.this.renderer);
                        table.setRowHeight(30);
                        MainFrame.this.hBoxResult.removeAll();
                        MainFrame.this.hBoxResult.add(new JScrollPane(table));
                        MainFrame.this.saveToTextMenuItem.setEnabled(true);
                        MainFrame.this.saveToGraphicsMenuItem.setEnabled(true);
                        MainFrame.this.saveToCSVMenuItem.setEnabled(true);
                        MainFrame.this.searchValueMenuItem.setEnabled(true);
                        MainFrame.this.searchRangeMenuItem.setEnabled(true);
                        MainFrame.this.getContentPane().validate();
                    }

                } catch (NumberFormatException var6) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", 2);
                }

            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.textFieldFrom.setText("0.0");
                MainFrame.this.textFieldTo.setText("1.0");
                MainFrame.this.textFieldStep.setText("0.1");
                MainFrame.this.hBoxResult.removeAll();
                MainFrame.this.hBoxResult.add(new JPanel());
                MainFrame.this.saveToTextMenuItem.setEnabled(false);
                MainFrame.this.saveToGraphicsMenuItem.setEnabled(false);
                MainFrame.this.searchValueMenuItem.setEnabled(false);
                MainFrame.this.searchRangeMenuItem.setEnabled(false);
                MainFrame.this.renderer.setNeedle((String) null);
                MainFrame.this.renderer.setRangeStart((String) null);
                MainFrame.this.renderer.setRangeEnd((String) null);
                MainFrame.this.getContentPane().validate();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension((int) hboxButtons.getMaximumSize().getWidth(), (int) hboxButtons.getMinimumSize().getHeight() * 2));
        this.getContentPane().add(hboxButtons, "South");
    }

    private void addRangeBox() {
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        this.textFieldFrom = new JTextField("0.0", 10);
        this.textFieldFrom.setMaximumSize(this.textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        this.textFieldTo = new JTextField("1.0", 10);
        this.textFieldTo.setMaximumSize(this.textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        this.textFieldStep = new JTextField("0.1", 10);
        this.textFieldStep.setMaximumSize(this.textFieldStep.getPreferredSize());
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(this.textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(this.textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(this.textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.setPreferredSize(new Dimension((int) hboxRange.getMaximumSize().getWidth(), (int) hboxRange.getMinimumSize().getHeight() * 2));
        this.getContentPane().add(hboxRange, "North");
    }

    public MainFrame(Double[] coefficients) {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;
        this.setSize(700, 500);
        Toolkit kit = Toolkit.getDefaultToolkit();
        this.setLocation((kit.getScreenSize().width - 700) / 2, (kit.getScreenSize().height - 500) / 2);
        this.initMenu();
        this.addRangeBox();
        this.initButtons();
        this.hBoxResult = Box.createHorizontalBox();
        this.hBoxResult.add(new JPanel());
        this.getContentPane().add(this.hBoxResult, "Center");
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }

        Double[] coefficients = new Double[args.length];
        int i = 0;

        try {
            String[] var6 = args;
            int var5 = args.length;

            for (int var4 = 0; var4 < var5; ++var4) {
                String arg = var6[var4];
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException var7) {
            System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
            System.exit(-2);
        }

        new MainFrame(coefficients);
    }
}
