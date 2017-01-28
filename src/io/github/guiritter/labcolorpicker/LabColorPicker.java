package io.github.guiritter.labcolorpicker;

/**
 *
 * @author Guilherme Alan Ritter
 */
public class LabColorPicker extends javax.swing.JFrame {

    static {
        javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
    }

    public LabColorPicker() {
        initComponents();
        upperSource = UpperSources.none;
        LabSpinner.setValue(50);
        aSpinner.setValue(0);
        bSpinner.setValue(0);
        i = new int[3];
        d = new double[3];
        panel = new WritableCanvas(257, 257);
        panel.setBounds(10, 10, 257, 257);
        panel.setVisible(true);
        add(panel);
        Rpanel = new WritableCanvas(20, 20);
        Gpanel = new WritableCanvas(20, 20);
        Bpanel = new WritableCanvas(20, 20);
        Hpanel = new WritableCanvas(20, 20);
        Spanel = new WritableCanvas(20, 20);
        Lpanel = new WritableCanvas(20, 20);
        Rpanel.setBounds(240, 280, 20, 20);
        Gpanel.setBounds(240, 300, 20, 20);
        Bpanel.setBounds(240, 320, 20, 20);
        Hpanel.setBounds(330, 280, 20, 20);
        Spanel.setBounds(330, 300, 20, 20);
        Lpanel.setBounds(330, 320, 20, 20);
        Rpanel.setVisible(true);
        Gpanel.setVisible(true);
        Rpanel.setVisible(true);
        Hpanel.setVisible(true);
        Spanel.setVisible(true);
        Lpanel.setVisible(true);
        add(Rpanel);
        add(Gpanel);
        add(Bpanel);
        add(Hpanel);
        add(Spanel);
        add(Lpanel);
        matrix = new WritableCanvas[11][11];
        for (fb = 0; fb < 11; fb++) {
            for (fa = 0; fa < 11; fa++) {
                matrix[fa][fb] = new WritableCanvas(20, 20);
                matrix[fa][fb].setBounds((fa * 20) + 280, (fb * 20) + 10, 20, 20);
                matrix[fa][fb].setVisible(true);
                add(matrix[fa][fb]);
            }
        }
        LabPanel = new WritableCanvas(20, 60);
        LabPanel.setBounds(240, 360, 20, 60);
        LabPanel.setVisible(true);
        add(LabPanel);
        drawColors();
        drawLab();
        drawMatrix();
        drawRGB();
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aSpinner.setValue((int)(evt.getX() - 128));
                bSpinner.setValue((int)(128 - evt.getY()));
            }
        });
    }

    private static void drawColors () {
        Li = (int) LabSpinner.getValue();
        for (ai = -128; ai <= 128; ai++) {
            for (bi = -128; bi <= 128; bi++) {
                LabToXYZ(Li, ai, bi);
                XYZToRGB(X, Y, Z);
                if (maskCheckBox.isSelected() && invalid) {
                    R = 118;
                    G = 118;
                    B = 118;
                }
                i[0] = (int) R;
                i[1] = (int) G;
                i[2] = (int) B;
                panel.setPixel(ai + 128, -bi + 128, i);
            }
        }
        drawRGB();
        panel.repaint();
    }

    private static void drawLab () {
        i[0] = (int) RSpinner.getValue();
        i[1] = (int) GSpinner.getValue();
        i[2] = (int) BSpinner.getValue();
        LabPanel.fill(i);
        RGBToXYZ(i[0], i[1], i[2]);
        XYZToLab(X, Y, Z);
        LabValueLabel.setText((int) Ld + "");
        aValueLabel.setText((int) ad + "");
        bValueLabel.setText((int) bd + "");
    }

    private static void drawMatrix () {
        Li = (int) LabSpinner.getValue();
        ai = (int) aSpinner.getValue();
        bi = (int) bSpinner.getValue();
        for (fb = 0; fb < 11; fb++) {
            for (fa = 0; fa < 11; fa++) {
                LabToXYZ(Li, fa - 5 + ai, fb - 5 + bi);
                XYZToRGB(X, Y, Z);
                if (maskCheckBox.isSelected() && invalid) {
                    R = 118;
                    G = 118;
                    B = 118;
                }
                i[0] = (int) R;
                i[1] = (int) G;
                i[2] = (int) B;
                matrix[fa][-fb + 10].fill(i);
            }
        }
    }

    private static void drawRGB () {
        LabToXYZ((int) LabSpinner.getValue(), (int) aSpinner.getValue(), (int) bSpinner.getValue());
        XYZToRGB(X, Y, Z);
        if (invalid) {
            i[0] = 118;
            i[1] = 118;
            i[2] = 118;
            Rpanel.fill(i);
            Gpanel.fill(i);
            Bpanel.fill(i);
            Hpanel.fill(i);
            Spanel.fill(i);
            Lpanel.fill(i);
            RValueLabel.setText("");
            GValueLabel.setText("");
            BValueLabel.setText("");
            HValueLabel.setText("");
            SValueLabel.setText("");
            LValueLabel.setText("");
        } else {
            HexField.setText("0x");
            i[0] = (int) R;
            i[1] = 0;
            i[2] = 0;
            Rpanel.fill(i);
            RValueLabel.setText(i[0] + "");
            HexField.setText(HexField.getText() + String.format("%02X", i[0]));
            i[0] = 0;
            i[1] = (int) G;
            i[2] = 0;
            Gpanel.fill(i);
            GValueLabel.setText(i[1] + "");
            HexField.setText(HexField.getText() + String.format("%02X", i[1]));
            i[0] = 0;
            i[1] = 0;
            i[2] = (int) B;
            Bpanel.fill(i);
            BValueLabel.setText(i[2] + "");
            HexField.setText(HexField.getText() + String.format("%02X", i[2]));
            RGBToHSL(R, G, B);
            i[0] = (int)(L * 255.0);
            i[1] = (int)(L * 255.0);
            i[2] = (int)(L * 255.0);
            Lpanel.fill(i);
            H *= 360.0;
            S *= 100.0;
            L *= 100.0;
            Hpanel.fill(HToFullRGB());
            HValueLabel.setText(((int)H) + "");
            SValueLabel.setText(((int)S) + "");
            LValueLabel.setText(((int)L) + "");
            H = 120.0 / 360.0;
            S /= 100.0;
            L = 50.0 / 100.0;
            HSLToRGB(H, S, L);
            i[0] = (int) R;
            i[1] = (int) G;
            i[2] = (int) B;
            Spanel.fill(i);
        }
    }

    public static double HueToRGB (double v1, double v2, double vH) {
        if (vH < 0.0) {
            vH += 1.0;
        }
        if (vH > 1.0) {
            vH -= 1.0;
        }
        if ((6.0 * vH) < 1.0) {
            return v1 + (v2 - v1) * 6.0 * vH;
        }
        if ((2.0 * vH) < 1.0) {
            return v2;
        }
        if ((3.0 * vH) < 2.0) {
            return v1 + (v2 - v1) * ((2.0 / 3.0) - vH) * 6.0;
        }
        return v1;
    }

    public static double[] HSLToRGB (double Hp, double Sp, double Lp) {
        if (Sp == 0.0) {
            R = Lp * 255.0;
            G = Lp * 255.0;
            B = Lp * 255.0;
        } else {
            if (Lp < 0.5) {
                v2 = Lp * (1.0 + Sp);
            } else {
                v2 = (Lp + Sp) - (Sp * Lp);
            }
            v1 = (2.0 * Lp) - v2;
            R = 255.0 * HueToRGB(v1, v2, Hp + (1.0 / 3.0));
            G = 255.0 * HueToRGB(v1, v2, Hp);
            B = 255.0 * HueToRGB(v1, v2, Hp - (1.0 / 3.0));
        }
        d[0] = R;
        d[1] = G;
        d[2] = B;
        return d;
    }

    private static int[] HToFullRGB () {
        if (H == 0) {
            d[0] = 255.0;
            d[1] = 0;
            d[2] = 0;
        } else if (H > 0 && H < 60.0) {
            d[0] = 255.0;
            d[1] = 255.0 * (H / 60.0);
            d[2] = 0;
        } else if (H == 60) {
            d[0] = 255.0;
            d[1] = 255.0;
            d[2] = 0;
        } else if (H > 60.0 && H < 120.0) {
            d[0] = 255.0 * ((120.0 - H) / 60.0);
            d[1] = 255.0;
            d[2] = 0;
        } else if (H == 120) {
            d[0] = 0;
            d[1] = 255.0;
            d[2] = 0;
        } else if (H > 120.0 && H < 180.0) {
            d[0] = 0;
            d[1] = 255.0;
            d[2] = 255.0 * ((H - 120.0) / 60.0);
        } else if (H == 180) {
            d[0] = 0;
            d[1] = 255.0;
            d[2] = 255.0;
        } else if (H > 180.0 && H < 240.0) {
            d[0] = 0;
            d[1] = 255.0 * ((240.0 - H) / 60.0);
            d[2] = 255.0;
        } else if (H == 240) {
            d[0] = 0;
            d[1] = 0;
            d[2] = 255.0;
        } else if (H > 240.0 && H < 300.0) {
            d[0] = 255.0 * ((H - 240.0) / 60.0);
            d[1] = 0;
            d[2] = 255.0;
        } else if (H == 300) {
            d[0] = 255.0;
            d[1] = 0;
            d[2] = 255.0;
        } else if (H > 300.0 && H < 360.0) {
            d[0] = 255.0;
            d[1] = 0;
            d[2] = 255.0 * ((360.0 - H) / 60.0);
        }
        i[0] = (int) d[0];
        i[1] = (int) d[1];
        i[2] = (int) d[2];
        return i;
    }

    public static int[] HToFullRGB (int Hp) {
        H = Hp;
        if (H == 0) {
            d[0] = 255.0;
            d[1] = 0;
            d[2] = 0;
        } else if (H > 0 && H < 60.0) {
            d[0] = 255.0;
            d[1] = 255.0 * (H / 60.0);
            d[2] = 0;
        } else if (H == 60) {
            d[0] = 255.0;
            d[1] = 255.0;
            d[2] = 0;
        } else if (H > 60.0 && H < 120.0) {
            d[0] = 255.0 * ((120.0 - H) / 60.0);
            d[1] = 255.0;
            d[2] = 0;
        } else if (H == 120) {
            d[0] = 0;
            d[1] = 255.0;
            d[2] = 0;
        } else if (H > 120.0 && H < 180.0) {
            d[0] = 0;
            d[1] = 255.0;
            d[2] = 255.0 * ((H - 120.0) / 60.0);
        } else if (H == 180) {
            d[0] = 0;
            d[1] = 255.0;
            d[2] = 255.0;
        } else if (H > 180.0 && H < 240.0) {
            d[0] = 0;
            d[1] = 255.0 * ((240.0 - H) / 60.0);
            d[2] = 255.0;
        } else if (H == 240) {
            d[0] = 0;
            d[1] = 0;
            d[2] = 255.0;
        } else if (H > 240.0 && H < 300.0) {
            d[0] = 255.0 * ((H - 240.0) / 60.0);
            d[1] = 0;
            d[2] = 255.0;
        } else if (H == 300) {
            d[0] = 255.0;
            d[1] = 0;
            d[2] = 255.0;
        } else if (H > 300.0 && H < 360.0) {
            d[0] = 255.0;
            d[1] = 0;
            d[2] = 255.0 * ((360.0 - H) / 60.0);
        }
        i[0] = (int) d[0];
        i[1] = (int) d[1];
        i[2] = (int) d[2];
        return i;
    }

    public static double[] LabToXYZ (double L, double a, double b) {
        Y = (L + 16.0) / 116.0;
        X = (a / 500.0) + Y;
        Z = Y - (b / 200.0);
        if (Math.pow(Y, 3.0) > 0.008856) {
            Y = Math.pow(Y, 3.0);
        } else {
            Y = (Y - (16.0 / 116.0)) / 7.787;
        }
        if (Math.pow(X, 3.0) > 0.008856) {
            X = Math.pow(X, 3.0);
        } else {
            X = (X - (16.0 / 116.0)) / 7.787;
        }
        if (Math.pow(Z, 3.0) > 0.008856) {
            Z = Math.pow(Z, 3.0);
        } else {
            Z = (Z - (16.0 / 116.0)) / 7.787;
        }
        X *= ref_X;
        Y *= ref_Y;
        Z *= ref_Z;
        d[0] = X;
        d[1] = Y;
        d[2] = Z;
        return d;
    }

    public static double[] RGBToHSL (double Rp, double Gp, double Bp) {
        R = Rp / 255.0;
        G = Gp / 255.0;
        B = Bp / 255.0;
        RGBmin = Math.min(Math.min(R, G), B);
        RGBmax = Math.max(Math.max(R, G), B);
        Δ = RGBmax - RGBmin;
        L = (RGBmax + RGBmin) / 2.0;
        if (Δ == 0) {
            H = 0;
            S = 0;
        } else {
            if (L < 0.5) {
                S = Δ / (RGBmax + RGBmin);
            } else {
                S = Δ / (2.0 - RGBmax - RGBmin);
            }
            ΔR = (((RGBmax - R) / 6.0) + (Δ / 2.0)) / Δ;
            ΔG = (((RGBmax - G) / 6.0) + (Δ / 2.0)) / Δ;
            ΔB = (((RGBmax - B) / 6.0) + (Δ / 2.0)) / Δ;
            if (R == RGBmax) {
                H = ΔB - ΔG;
            } else if (G == RGBmax) {
                H = (1.0/3.0) + ΔR - ΔB;
            } else if (B == RGBmax) {
                H = (2.0/3.0) + ΔG - ΔR;
            }
            if (H < 0) {
                H++;
            }
            if (H > 1) {
                H--;
            }
        }
        d[0] = H;
        d[1] = S;
        d[2] = L;
        return d;
    }

    public static double[] RGBToXYZ (double Rp, double Gp, double Bp) {
        R = Rp / 255;
        G = Gp / 255;
        B = Bp / 255;
        if (R > 0.04045) {
            R = Math.pow(((R + 0.055) / 1.055), 2.4);
        } else {
            R = R / 12.92;
        }
        if (G > 0.04045) {
            G = Math.pow(((G + 0.055) / 1.055), 2.4);
        } else {
            G = G / 12.92;
        }
        if (B > 0.04045) {
            B = Math.pow(((B + 0.055) / 1.055), 2.4);
        } else {
            B = B / 12.92;
        }
        R *= 100;
        G *= 100;
        B *= 100;
        X = (R * 0.4124) + (G * 0.3576) + (B * 0.1805);
        Y = (R * 0.2126) + (G * 0.7152) + (B * 0.0722);
        Z = (R * 0.0193) + (G * 0.1192) + (B * 0.9505);
        d[0] = X;
        d[1] = Y;
        d[2] = Z;
        return d;
    }

    public static double[] XYZToLab (double Xp, double Yp, double Zp) {
        X = Xp / ref_X;
        Y = Yp / ref_Y;
        Z = Zp / ref_Z;
        if (X > 0.008856) {
            X = Math.pow(X, 1.0/3.0);
        } else {
            X = (7.787 * X) + (16.0 / 116.0);
        }
        if (Y > 0.008856) {
            Y = Math.pow(Y, 1.0/3.0);
        } else {
            Y = (7.787 * Y) + (16.0 / 116.0);
        }
        if (Z > 0.008856) {
            Z = Math.pow(Z, 1.0/3.0);
        } else {
            Z = (7.787 * Z) + (16.0 / 116.0);
        }
        Ld = (116 * Y) - 16;
        ad = 500 * (X - Y);
        bd = 200 * (Y - Z);
        d[0] = Ld;
        d[1] = ad;
        d[2] = bd;
        return d;
    }

    public static double[] XYZToRGB (double Xp, double Yp, double Zp) {
        X = Xp / 100;
        Y = Yp / 100;
        Z = Zp / 100;
        R = (X *  3.2406) + (Y * -1.5372) + (Z * -0.4986);
        G = (X * -0.9689) + (Y *  1.8758) + (Z *  0.0415);
        B = (X *  0.0557) + (Y * -0.2040) + (Z *  1.0570);
        if (R > 0.0031308) {
            R = (1.055 * (Math.pow(R, 1 / 2.4))) - 0.055;
        } else {
            R = 12.92 * R;
        }
        if (G > 0.0031308) {
            G = (1.055 * (Math.pow(G, 1 / 2.4))) - 0.055;
        } else {
            G = 12.92 * G;
        }
        if (B > 0.0031308) {
            B = (1.055 * (Math.pow(B, 1 / 2.4))) - 0.055;
        } else {
            B = 12.92 * B;
        }
        R *= 255;
        G *= 255;
        B *= 255;
        d[0] = R;
        d[1] = G;
        d[2] = B;
        invalid = false;
        if (R > 255 || R < 0) {
            invalid = true;
        } else if (G > 255 || G < 0) {
            invalid = true;
        } else if (B > 255 || B < 0) {
            invalid = true;
        }
        return d;
    }

    @SuppressWarnings({"unchecked"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LabSpinner = new javax.swing.JSpinner();
        bSpinner = new javax.swing.JSpinner();
        aSetLabel = new javax.swing.JLabel();
        aSpinner = new javax.swing.JSpinner();
        bSetLabel = new javax.swing.JLabel();
        maskCheckBox = new javax.swing.JCheckBox();
        LabSetLabel = new javax.swing.JLabel();
        BGetLabel = new javax.swing.JLabel();
        BValueLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator0 = new javax.swing.JSeparator();
        GGetLabel = new javax.swing.JLabel();
        GValueLabel = new javax.swing.JLabel();
        RGetLabel = new javax.swing.JLabel();
        RValueLabel = new javax.swing.JLabel();
        HGetLabel = new javax.swing.JLabel();
        HValueLabel = new javax.swing.JLabel();
        SValueLabel = new javax.swing.JLabel();
        SGetLabel = new javax.swing.JLabel();
        LGetLabel = new javax.swing.JLabel();
        LValueLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        RSetLabel = new javax.swing.JLabel();
        GSetLabel = new javax.swing.JLabel();
        BSetLabel = new javax.swing.JLabel();
        RSpinner = new javax.swing.JSpinner();
        BSpinner = new javax.swing.JSpinner();
        GSpinner = new javax.swing.JSpinner();
        HSetLabel = new javax.swing.JLabel();
        SSetLabel = new javax.swing.JLabel();
        LSetLabel = new javax.swing.JLabel();
        HSpinner = new javax.swing.JSpinner();
        SSpinner = new javax.swing.JSpinner();
        LSpinner = new javax.swing.JSpinner();
        LabGetLabel = new javax.swing.JLabel();
        aGetLabel = new javax.swing.JLabel();
        bGetLabel = new javax.swing.JLabel();
        LabValueLabel = new javax.swing.JLabel();
        aValueLabel = new javax.swing.JLabel();
        bValueLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        θSetLabel = new javax.swing.JLabel();
        rSetLabel = new javax.swing.JLabel();
        θSpinner = new javax.swing.JSpinner();
        rSpinner = new javax.swing.JSpinner();
        HexField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        getContentPane().setLayout(null);

        LabSpinner.setFont(getFont());
        LabSpinner.setModel(new javax.swing.SpinnerNumberModel(50, 0, 100, 1));
        LabSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        LabSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                LabSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(LabSpinner);
        LabSpinner.setBounds(30, 280, 50, 20);

        bSpinner.setFont(getFont());
        bSpinner.setModel(new javax.swing.SpinnerNumberModel(0, -128, 128, 1));
        bSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(bSpinner);
        bSpinner.setBounds(30, 320, 50, 20);

        aSetLabel.setFont(LabSpinner.getFont());
        aSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        aSetLabel.setText("a: ");
        getContentPane().add(aSetLabel);
        aSetLabel.setBounds(10, 300, 20, 20);

        aSpinner.setFont(getFont());
        aSpinner.setModel(new javax.swing.SpinnerNumberModel(0, -128, 128, 1));
        aSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        aSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                aSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(aSpinner);
        aSpinner.setBounds(30, 300, 50, 20);

        bSetLabel.setFont(LabSpinner.getFont());
        bSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        bSetLabel.setText("b: ");
        getContentPane().add(bSetLabel);
        bSetLabel.setBounds(10, 320, 20, 20);

        maskCheckBox.setFont(LabSpinner.getFont());
        maskCheckBox.setSelected(true);
        maskCheckBox.setText("mask");
        maskCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                maskCheckBoxStateChanged(evt);
            }
        });
        getContentPane().add(maskCheckBox);
        maskCheckBox.setBounds(370, 320, 60, 20);

        LabSetLabel.setFont(LabSpinner.getFont());
        LabSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        LabSetLabel.setText("L: ");
        getContentPane().add(LabSetLabel);
        LabSetLabel.setBounds(10, 280, 20, 20);

        BGetLabel.setFont(LabSpinner.getFont());
        BGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        BGetLabel.setText("B: ");
        getContentPane().add(BGetLabel);
        BGetLabel.setBounds(190, 320, 20, 20);

        BValueLabel.setFont(LabSpinner.getFont());
        BValueLabel.setText("255");
        getContentPane().add(BValueLabel);
        BValueLabel.setBounds(210, 320, 30, 20);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(270, 280, 10, 140);

        jSeparator0.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator0);
        jSeparator0.setBounds(90, 280, 10, 140);

        GGetLabel.setFont(LabSpinner.getFont());
        GGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        GGetLabel.setText("G: ");
        getContentPane().add(GGetLabel);
        GGetLabel.setBounds(190, 300, 20, 20);

        GValueLabel.setFont(LabSpinner.getFont());
        GValueLabel.setText("255");
        getContentPane().add(GValueLabel);
        GValueLabel.setBounds(210, 300, 30, 20);

        RGetLabel.setFont(LabSpinner.getFont());
        RGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        RGetLabel.setText("R: ");
        getContentPane().add(RGetLabel);
        RGetLabel.setBounds(190, 280, 20, 20);

        RValueLabel.setFont(LabSpinner.getFont());
        RValueLabel.setText("255");
        getContentPane().add(RValueLabel);
        RValueLabel.setBounds(210, 280, 30, 20);

        HGetLabel.setFont(LabSpinner.getFont());
        HGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        HGetLabel.setText("H: ");
        getContentPane().add(HGetLabel);
        HGetLabel.setBounds(280, 280, 20, 20);

        HValueLabel.setFont(LabSpinner.getFont());
        HValueLabel.setText("180");
        getContentPane().add(HValueLabel);
        HValueLabel.setBounds(300, 280, 30, 20);

        SValueLabel.setFont(LabSpinner.getFont());
        SValueLabel.setText("100");
        getContentPane().add(SValueLabel);
        SValueLabel.setBounds(300, 300, 30, 20);

        SGetLabel.setFont(LabSpinner.getFont());
        SGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        SGetLabel.setText("S: ");
        getContentPane().add(SGetLabel);
        SGetLabel.setBounds(280, 300, 20, 20);

        LGetLabel.setFont(LabSpinner.getFont());
        LGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        LGetLabel.setText("L: ");
        getContentPane().add(LGetLabel);
        LGetLabel.setBounds(280, 320, 20, 20);

        LValueLabel.setFont(LabSpinner.getFont());
        LValueLabel.setText("50");
        getContentPane().add(LValueLabel);
        LValueLabel.setBounds(300, 320, 30, 20);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(180, 280, 10, 140);
        getContentPane().add(jSeparator3);
        jSeparator3.setBounds(10, 350, 430, 10);

        RSetLabel.setFont(LabSpinner.getFont());
        RSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        RSetLabel.setText("R: ");
        getContentPane().add(RSetLabel);
        RSetLabel.setBounds(10, 360, 20, 20);

        GSetLabel.setFont(LabSpinner.getFont());
        GSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        GSetLabel.setText("G: ");
        getContentPane().add(GSetLabel);
        GSetLabel.setBounds(10, 380, 20, 20);

        BSetLabel.setFont(LabSpinner.getFont());
        BSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        BSetLabel.setText("B: ");
        getContentPane().add(BSetLabel);
        BSetLabel.setBounds(10, 400, 20, 20);

        RSpinner.setFont(getFont());
        RSpinner.setModel(new javax.swing.SpinnerNumberModel(128, 0, 255, 1));
        RSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        RSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                RSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(RSpinner);
        RSpinner.setBounds(30, 360, 50, 20);

        BSpinner.setFont(getFont());
        BSpinner.setModel(new javax.swing.SpinnerNumberModel(128, 0, 255, 1));
        BSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        BSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                BSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(BSpinner);
        BSpinner.setBounds(30, 400, 50, 20);

        GSpinner.setFont(getFont());
        GSpinner.setModel(new javax.swing.SpinnerNumberModel(128, 0, 255, 1));
        GSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        GSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                GSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(GSpinner);
        GSpinner.setBounds(30, 380, 50, 20);

        HSetLabel.setFont(LabSpinner.getFont());
        HSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        HSetLabel.setText("H: ");
        HSetLabel.setEnabled(false);
        getContentPane().add(HSetLabel);
        HSetLabel.setBounds(100, 360, 20, 20);

        SSetLabel.setFont(LabSpinner.getFont());
        SSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        SSetLabel.setText("S: ");
        SSetLabel.setEnabled(false);
        getContentPane().add(SSetLabel);
        SSetLabel.setBounds(100, 380, 20, 20);

        LSetLabel.setFont(LabSpinner.getFont());
        LSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        LSetLabel.setText("L: ");
        LSetLabel.setEnabled(false);
        getContentPane().add(LSetLabel);
        LSetLabel.setBounds(100, 400, 20, 20);

        HSpinner.setFont(getFont());
        HSpinner.setModel(new javax.swing.SpinnerNumberModel(180, 0, 359, 1));
        HSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        HSpinner.setEnabled(false);
        HSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                HSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(HSpinner);
        HSpinner.setBounds(120, 360, 50, 20);

        SSpinner.setFont(getFont());
        SSpinner.setModel(new javax.swing.SpinnerNumberModel(100, 0, 100, 1));
        SSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        SSpinner.setEnabled(false);
        SSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(SSpinner);
        SSpinner.setBounds(120, 380, 50, 20);

        LSpinner.setFont(getFont());
        LSpinner.setModel(new javax.swing.SpinnerNumberModel(50, 0, 100, 1));
        LSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        LSpinner.setEnabled(false);
        LSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                LSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(LSpinner);
        LSpinner.setBounds(120, 400, 50, 20);

        LabGetLabel.setFont(LabSpinner.getFont());
        LabGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        LabGetLabel.setText("L: ");
        getContentPane().add(LabGetLabel);
        LabGetLabel.setBounds(190, 360, 20, 20);

        aGetLabel.setFont(LabSpinner.getFont());
        aGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        aGetLabel.setText("a: ");
        getContentPane().add(aGetLabel);
        aGetLabel.setBounds(190, 380, 20, 20);

        bGetLabel.setFont(LabSpinner.getFont());
        bGetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        bGetLabel.setText("b: ");
        getContentPane().add(bGetLabel);
        bGetLabel.setBounds(190, 400, 20, 20);

        LabValueLabel.setFont(LabSpinner.getFont());
        LabValueLabel.setText("50");
        getContentPane().add(LabValueLabel);
        LabValueLabel.setBounds(210, 360, 30, 20);

        aValueLabel.setFont(LabSpinner.getFont());
        aValueLabel.setText("0");
        getContentPane().add(aValueLabel);
        aValueLabel.setBounds(210, 380, 30, 20);

        bValueLabel.setFont(LabSpinner.getFont());
        bValueLabel.setText("0");
        getContentPane().add(bValueLabel);
        bValueLabel.setBounds(210, 400, 30, 20);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator4);
        jSeparator4.setBounds(360, 280, 10, 140);

        θSetLabel.setFont(LabSpinner.getFont());
        θSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        θSetLabel.setText("θ: ");
        getContentPane().add(θSetLabel);
        θSetLabel.setBounds(100, 300, 20, 20);

        rSetLabel.setFont(LabSpinner.getFont());
        rSetLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        rSetLabel.setText("r: ");
        getContentPane().add(rSetLabel);
        rSetLabel.setBounds(100, 320, 20, 20);

        θSpinner.setFont(getFont());
        θSpinner.setModel(new javax.swing.SpinnerNumberModel(0, -1, 360, 1));
        θSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        θSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                θSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(θSpinner);
        θSpinner.setBounds(120, 300, 50, 20);

        rSpinner.setFont(getFont());
        rSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 182, 1));
        rSpinner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        rSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rSpinnerStateChanged(evt);
            }
        });
        getContentPane().add(rSpinner);
        rSpinner.setBounds(120, 320, 50, 20);

        HexField.setEditable(false);
        HexField.setFont(getFont());
        HexField.setText("0xABCDEF");
        HexField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(HexField);
        HexField.setBounds(370, 280, 70, 20);

        setSize(new java.awt.Dimension(526, 468));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void LabSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_LabSpinnerStateChanged
        drawColors();
        drawMatrix();
    }//GEN-LAST:event_LabSpinnerStateChanged

    private void maskCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_maskCheckBoxStateChanged
        drawColors();
        drawMatrix();
    }//GEN-LAST:event_maskCheckBoxStateChanged

    private void aSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_aSpinnerStateChanged
        if (upperSource == UpperSources.none) {
            upperSource = UpperSources.a;
            drawRGB();
            drawMatrix();
            ai = (int) aSpinner.getValue();
            bi = (int) bSpinner.getValue();
            if (ai == 0) {
                if (bi == 0) {
                    θSpinner.setValue((int)0);
                } else if (bi > 0) {
                    θSpinner.setValue((int)90);
                } else {
                    θSpinner.setValue((int)270);
                }
            } else if (ai > 0) {
                if (bi == 0) {
                    θSpinner.setValue((int)0);
                } else if (bi > 0) {
                    θSpinner.setValue((int)Math.toDegrees(Math.atan2(bi, ai)));
                } else {
                    θSpinner.setValue((int)(360 - Math.toDegrees(Math.atan2(-bi, ai))));
                }
            } else {
                if (bi == 0) {
                    θSpinner.setValue((int)180);
                } else if (bi > 0) {
                    θSpinner.setValue((int)(180 - Math.toDegrees(Math.atan2(bi, -ai))));
                } else {
                    θSpinner.setValue((int)(180 + Math.toDegrees(Math.atan2(-bi, -ai))));
                }
            }
            rSpinner.setValue((int)Math.sqrt(Math.pow(ai, 2) + Math.pow(bi, 2)));
            upperSource = UpperSources.none;
        }
    }//GEN-LAST:event_aSpinnerStateChanged

    private void bSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bSpinnerStateChanged
        if (upperSource == UpperSources.none) {
            upperSource = UpperSources.b;
            drawRGB();
            drawMatrix();
            ai = (int) aSpinner.getValue();
            bi = (int) bSpinner.getValue();
            if (ai == 0) {
                if (bi == 0) {
                    θSpinner.setValue((int)0);
                } else if (bi > 0) {
                    θSpinner.setValue((int)90);
                } else {
                    θSpinner.setValue((int)270);
                }
            } else if (ai > 0) {
                if (bi == 0) {
                    θSpinner.setValue((int)0);
                } else if (bi > 0) {
                    θSpinner.setValue((int)Math.toDegrees(Math.atan2(bi, ai)));
                } else {
                    θSpinner.setValue((int)(360 - Math.toDegrees(Math.atan2(-bi, ai))));
                }
            } else {
                if (bi == 0) {
                    θSpinner.setValue((int)180);
                } else if (bi > 0) {
                    θSpinner.setValue((int)(180 - Math.toDegrees(Math.atan2(bi, -ai))));
                } else {
                    θSpinner.setValue((int)(180 + Math.toDegrees(Math.atan2(-bi, -ai))));
                }
            }
            rSpinner.setValue((int)Math.sqrt(Math.pow(ai, 2) + Math.pow(bi, 2)));
            upperSource = UpperSources.none;
        }
    }//GEN-LAST:event_bSpinnerStateChanged

    private void RSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_RSpinnerStateChanged
        drawLab();
    }//GEN-LAST:event_RSpinnerStateChanged

    private void BSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_BSpinnerStateChanged
        drawLab();
    }//GEN-LAST:event_BSpinnerStateChanged

    private void GSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_GSpinnerStateChanged
        drawLab();
    }//GEN-LAST:event_GSpinnerStateChanged

    private void HSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_HSpinnerStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_HSpinnerStateChanged

    private void SSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SSpinnerStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SSpinnerStateChanged

    private void LSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_LSpinnerStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_LSpinnerStateChanged

    private void θSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_θSpinnerStateChanged
        if (((int)θSpinner.getValue()) == -1) {
            θSpinner.setValue((int)359);
        } else if (((int)θSpinner.getValue()) == 360) {
            θSpinner.setValue((int)0);
        }
        if (upperSource == UpperSources.none) {
            upperSource = UpperSources.θ;
            θi = (int) θSpinner.getValue();
            ri = (int) rSpinner.getValue();
            aSpinner.setValue((int)(Math.cos(Math.toRadians(θi))*(ri)));
            bSpinner.setValue((int)(Math.sin(Math.toRadians(θi))*(ri)));
            drawRGB();
            drawMatrix();
            upperSource = UpperSources.none;
        }
    }//GEN-LAST:event_θSpinnerStateChanged

    private void rSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rSpinnerStateChanged
        if (upperSource == UpperSources.none) {
            upperSource = UpperSources.r;
            θi = (int) θSpinner.getValue();
            ri = (int) rSpinner.getValue();
            aSpinner.setValue((int)(Math.cos(Math.toRadians(θi))*(ri)));
            bSpinner.setValue((int)(Math.sin(Math.toRadians(θi))*(ri)));
            drawRGB();
            drawMatrix();
            upperSource = UpperSources.none;
        }
    }//GEN-LAST:event_rSpinnerStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new LabColorPicker().setVisible(true);
            }
        });
    }

    private static enum UpperSources {none, a, b, θ, r};
    private static enum LowerSources {none, R, G, B, H, S, L};

    private static int i[] = null;
    private static double d[] = null;
    private static double Δ = 0, ΔR = 0, ΔG = 0, ΔB = 0;
    private static int fa = 0, fb = 0;
    private static double H = 0, S = 0, L = 0;
    private static WritableCanvas Hpanel = null, Spanel = null, Lpanel = null;
    private static boolean invalid = false;
    private static int Li = 0, ai = 0, bi = 0;
    private static double Ld = 0, ad = 0, bd = 0;
    private static WritableCanvas LabPanel = null;
    private static WritableCanvas matrix[][] = null;
    private static WritableCanvas panel = null;
    public static final double ref_X = 95.047;
    public static final double ref_Y = 100.0;
    public static final double ref_Z = 108.883;
    private static double R = 0, G = 0, B = 0;
    private static double RGBmin = 0, RGBmax;
    private static WritableCanvas Rpanel = null, Gpanel = null, Bpanel = null;
    private static int θi = 0, ri = 0;
    private static UpperSources upperSource = UpperSources.none;
    private static double v1 = 0, v2 = 0;
    private static double X = 0, Y = 0, Z = 0;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel BGetLabel;
    private static javax.swing.JLabel BSetLabel;
    private static javax.swing.JSpinner BSpinner;
    private static javax.swing.JLabel BValueLabel;
    private static javax.swing.JLabel GGetLabel;
    private static javax.swing.JLabel GSetLabel;
    private static javax.swing.JSpinner GSpinner;
    private static javax.swing.JLabel GValueLabel;
    private static javax.swing.JLabel HGetLabel;
    private static javax.swing.JLabel HSetLabel;
    private static javax.swing.JSpinner HSpinner;
    private static javax.swing.JLabel HValueLabel;
    private static javax.swing.JTextField HexField;
    private static javax.swing.JLabel LGetLabel;
    private static javax.swing.JLabel LSetLabel;
    private static javax.swing.JSpinner LSpinner;
    private static javax.swing.JLabel LValueLabel;
    private static javax.swing.JLabel LabGetLabel;
    private static javax.swing.JLabel LabSetLabel;
    private static javax.swing.JSpinner LabSpinner;
    private static javax.swing.JLabel LabValueLabel;
    private static javax.swing.JLabel RGetLabel;
    private static javax.swing.JLabel RSetLabel;
    private static javax.swing.JSpinner RSpinner;
    private static javax.swing.JLabel RValueLabel;
    private static javax.swing.JLabel SGetLabel;
    private static javax.swing.JLabel SSetLabel;
    private static javax.swing.JSpinner SSpinner;
    private static javax.swing.JLabel SValueLabel;
    private static javax.swing.JLabel aGetLabel;
    private static javax.swing.JLabel aSetLabel;
    private static javax.swing.JSpinner aSpinner;
    private static javax.swing.JLabel aValueLabel;
    private static javax.swing.JLabel bGetLabel;
    private static javax.swing.JLabel bSetLabel;
    private static javax.swing.JSpinner bSpinner;
    private static javax.swing.JLabel bValueLabel;
    private javax.swing.JSeparator jSeparator0;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private static javax.swing.JCheckBox maskCheckBox;
    private static javax.swing.JLabel rSetLabel;
    private static javax.swing.JSpinner rSpinner;
    private static javax.swing.JLabel θSetLabel;
    private static javax.swing.JSpinner θSpinner;
    // End of variables declaration//GEN-END:variables
}
