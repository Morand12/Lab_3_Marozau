
import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return this.from;
    }

    public Double getTo() {
        return this.to;
    }

    public Double getStep() {
        return this.step;
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return (int)Math.ceil((this.to - this.from) / this.step) + 1;
    }

    public Object getValueAt(int row, int col) {
        double x = this.from + this.step * (double)row;
        Double result;
        Double antiresult;
        int i;
        switch (col) {
            case 0:
            default:
                return x;
            case 1:
                result = 0.0;

                for(i = 0; i < this.coefficients.length; ++i) {
                    result = result + x * result + this.coefficients[this.coefficients.length - i - 1];
                }

                return result;
            case 2:
                antiresult = 0.0;

                for(i = 0; i < this.coefficients.length; ++i) {
                    antiresult = antiresult + x * antiresult + this.coefficients[i];
                }

                return antiresult;
            case 3:
                result = 0.0;
                antiresult = 0.0;

                for(i = 0; i < this.coefficients.length; ++i) {
                    result = result + x * result + this.coefficients[this.coefficients.length - i - 1];
                }

                for(i = 0; i < this.coefficients.length; ++i) {
                    antiresult = antiresult + x * antiresult + this.coefficients[i];
                }

                return result - antiresult;
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
            default:
                return "Значение многочлена";
            case 2:
                return "Зеркальные коэффициенты";
            case 3:
                return "Разность";
        }
    }

    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}

