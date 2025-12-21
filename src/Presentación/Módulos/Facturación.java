package Presentación.Módulos;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Facturación extends JPanel {

    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> comboEstado, comboCliente;

    public Facturación() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de botones esenciales
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setOpaque(false);

        Botón btnGenerarFactura = new Botón("Generar Factura", new Color(59, 130, 246));
        Botón btnConsultarFactura = new Botón("Consultar Factura", new Color(70, 128, 139));
        Botón btnAnularFactura = new Botón("Anular Factura", new Color(239, 68, 68));
        Botón btnRegistrarPago = new Botón("Registrar Pago", new Color(147, 51, 234));

        Dimension dimBoton = new Dimension(180, 40);
        btnGenerarFactura.setPreferredSize(dimBoton);
        btnConsultarFactura.setPreferredSize(dimBoton);
        btnAnularFactura.setPreferredSize(dimBoton);
        btnRegistrarPago.setPreferredSize(dimBoton);

        panelBotones.add(btnGenerarFactura);
        panelBotones.add(btnConsultarFactura);
        panelBotones.add(btnAnularFactura);
        panelBotones.add(btnRegistrarPago);

        add(panelBotones, BorderLayout.NORTH);

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.WHITE);
        txtBuscar = new JTextField(20);
        txtBuscar.setPreferredSize(new Dimension(200, 35));
        txtBuscar.setBackground(new Color(55, 65, 81));
        txtBuscar.setForeground(Color.WHITE);
        txtBuscar.setBorder(new LineBorder(new Color(75, 85, 99), 1));

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(Color.WHITE);
        comboEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Pagada", "Anulada", "Vencida"});
        comboEstado.setPreferredSize(new Dimension(150, 35));
        comboEstado.setBackground(new Color(55, 65, 81));
        comboEstado.setForeground(Color.WHITE);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setForeground(Color.WHITE);
        comboCliente = new JComboBox<>(new String[]{"Todos", "Cliente A", "Cliente B", "Cliente C"});
        comboCliente.setPreferredSize(new Dimension(150, 35));
        comboCliente.setBackground(new Color(55, 65, 81));
        comboCliente.setForeground(Color.WHITE);

        Botón btnFiltrar = new Botón("Filtrar", new Color(70, 128, 139));
        btnFiltrar.setPreferredSize(new Dimension(100, 35));

        panelFiltros.add(lblBuscar);
        panelFiltros.add(txtBuscar);
        panelFiltros.add(lblEstado);
        panelFiltros.add(comboEstado);
        panelFiltros.add(lblCliente);
        panelFiltros.add(comboCliente);
        panelFiltros.add(btnFiltrar);

        add(panelFiltros, BorderLayout.CENTER);

        // Tabla de facturas
        crearTablaFacturas();
        JScrollPane scrollTabla = new JScrollPane(tablaFacturas);
        scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
        scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));
        add(scrollTabla, BorderLayout.SOUTH);

        // Action Listeners simples
        btnGenerarFactura.addActionListener(e -> GestorAlertas.mostrarExito(this, "Generando factura..."));
        btnConsultarFactura.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Mostrando detalles de la factura"));
        btnAnularFactura.addActionListener(e -> GestorAlertas.mostrarAdvertencia(this, "¿Está seguro de anular esta factura?"));
        btnRegistrarPago.addActionListener(e -> GestorAlertas.mostrarExito(this, "Registro de pago completado"));
        btnFiltrar.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Aplicando filtros..."));
    }

    private void crearTablaFacturas() {
        String[] columnas = {"N° Factura", "Fecha", "Cliente", "Monto", "Estado", "Fecha Vencimiento", "Acciones"};
        Object[][] datos = {
            {"FAC-001", "20/12/2025", "Cliente A", "$1,250.00", "Pagada", "25/12/2025", "Ver"},
            {"FAC-002", "19/12/2025", "Cliente B", "$890.50", "Pendiente", "24/12/2025", "Ver"},

        };

        modeloTabla = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setBackground(new Color(31, 41, 55));
        tablaFacturas.setForeground(Color.WHITE);
        tablaFacturas.setGridColor(new Color(55, 65, 81));
        tablaFacturas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaFacturas.setRowHeight(35);
        tablaFacturas.setSelectionBackground(new Color(75, 85, 99));
        tablaFacturas.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaFacturas.getTableHeader();
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(31, 41, 55));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setBackground(new Color(31, 41, 55));
        render.setForeground(Color.WHITE);
        render.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tablaFacturas.getColumnCount(); i++) {
            tablaFacturas.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        tablaFacturas.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaFacturas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(5).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(6).setPreferredWidth(80);
    }
}
