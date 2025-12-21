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

public class Auditoría extends JPanel {

  private JTable tablaLogs;
  private DefaultTableModel modeloTabla;

  public Auditoría() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelHerramientas.setOpaque(false);

    Botón btnConsultar = new Botón("Consultar Actividades", new Color(70, 128, 139));
    Botón btnAlertas = new Botón("Alertas de Seguridad", new Color(239, 68, 68));
    Botón btnExportar = new Botón("Exportar Informe", new Color(249, 115, 22));

    Dimension dimBoton = new Dimension(170, 40);
    btnConsultar.setPreferredSize(dimBoton);
    btnAlertas.setPreferredSize(dimBoton);
    btnExportar.setPreferredSize(dimBoton);

    panelHerramientas.add(btnConsultar);
    panelHerramientas.add(btnAlertas);
    panelHerramientas.add(btnExportar);


    crearTabla();
    JScrollPane scrollTabla = new JScrollPane(tablaLogs);
    scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
    scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelHerramientas, BorderLayout.NORTH);
    add(scrollTabla, BorderLayout.CENTER);

    btnConsultar.addActionListener(e ->
      GestorAlertas.mostrarExito(this, "Consultar Actividades")
    );

    btnAlertas.addActionListener(e ->
      GestorAlertas.mostrarAdvertencia(this, "Mostrando alertas de seguridad registradas")
    );

    btnExportar.addActionListener(e ->
      GestorAlertas.mostrarExito(this, "Informe de auditoría exportado exitosamente")
    );
  }

  private void crearTabla() {
    String[] columnas = {
      "ID", "Fecha - Hora", "Usuario", "Acción",
      "Módulo", "Detalle"
    };

    Object[][] datos = {
      {"1", "20/12/2025 - 18:30", "admin", "LOGIN", "Seguridad", "Inicio de sesión exitoso"},
      {"2", "20/12/2025 - 18:15", "secre", "CREAR", "Clientes", "Registro de nuevo cliente"},
    };

    modeloTabla = new DefaultTableModel(datos, columnas) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaLogs = new JTable(modeloTabla);
    tablaLogs.setBackground(new Color(31, 41, 55));
    tablaLogs.setForeground(Color.WHITE);
    tablaLogs.setGridColor(new Color(55, 65, 81));
    tablaLogs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaLogs.setRowHeight(30);
    tablaLogs.setSelectionBackground(new Color(75, 85, 99));
    tablaLogs.setSelectionForeground(Color.WHITE);

    // Cabecera (Header)
    JTableHeader header = tablaLogs.getTableHeader();
    header.setBackground(new Color(243, 244, 246));
    header.setForeground(new Color(31, 41, 55));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    // Renderizador para alinear texto y mantener colores
    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setBackground(new Color(31, 41, 55));
    render.setForeground(Color.WHITE);
    render.setHorizontalAlignment(SwingConstants.LEFT);

    for (int i = 0; i < tablaLogs.getColumnCount(); i++) {
      tablaLogs.getColumnModel().getColumn(i).setCellRenderer(render);
    }

    tablaLogs.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaLogs.getColumnModel().getColumn(1).setPreferredWidth(140);
    tablaLogs.getColumnModel().getColumn(5).setPreferredWidth(220);
  }
}
