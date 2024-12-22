
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private String needle = null;
    private String rangeStart = null;
    private String rangeEnd = null;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        this.formatter.setMaximumFractionDigits(5);
        this.formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = this.formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        this.formatter.setDecimalFormatSymbols(dottedDouble);
        this.panel.add(this.label);
        this.panel.setLayout(new FlowLayout(0));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedDouble = this.formatter.format(value);
        this.label.setText(formattedDouble);
        if (col > 0 && col < 3 && this.needle != null && this.needle.equals(formattedDouble)) {
            this.panel.setBackground(Color.RED);
        } else if (col > 0 && col < 3 && this.rangeStart != null && this.rangeEnd != null && Double.parseDouble(this.rangeStart) < Double.parseDouble(formattedDouble) && Double.parseDouble(this.rangeEnd) > Double.parseDouble(formattedDouble)) {
            this.panel.setBackground(Color.BLUE);
        } else if ((col + row + 2) % 2 == 1) {
            this.panel.setBackground(Color.WHITE);
            this.label.setForeground(Color.BLACK);
        } else {
            this.panel.setBackground(Color.BLACK);
            this.label.setForeground(Color.WHITE);
        }

        return this.panel;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }
}
