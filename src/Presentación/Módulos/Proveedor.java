package Presentación.Módulos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Presentación.Recursos.Botón;

public class Proveedor extends JPanel {
  private static final String ADMIN_PASSWORD = "admin123";

  private JTable tabla;
  private DefaultTableModel modelo;
  private Botón botónAgregar, botónActualizar, botónServicios, botónEliminar;

  private Map<String, List<String>> historialServicios = new HashMap<>();
  private Map<String, List<String>> historialReclamos = new HashMap<>();

  // REQUISITO Anexo 2: Catálogo de servicios (Nombres amigables para el usuario)
  private static final String[] CATALOGO_SERVICIOS = {
    "Combustible",
    "Cambio de Llantas",
    "Cambio de Aceite",
    "Mecánica",
    "Repuestos y Accesorios"
  };

  // Regex estricto para email: requiere texto + @ + dominio + . + extensión (2 a 6 letras)
  private static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$";

  public Proveedor() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    // --- PANEL DE BOTONES ---
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelBotones.setOpaque(false);

    botónAgregar = new Botón("Registrar Proveedor", new Color(40, 167, 69));
    botónActualizar = new Botón("Actualizar Contacto", new Color(234, 177, 0));
    botónServicios = new Botón("Historial y Servicios", new Color(70, 128, 139));
    botónEliminar = new Botón("Eliminar", new Color(239, 68, 68));

    Dimension dimBoton = new Dimension(170, 40); // Un poco más ancho para leer mejor
    botónAgregar.setPreferredSize(dimBoton);
    botónActualizar.setPreferredSize(dimBoton);
    botónServicios.setPreferredSize(dimBoton);
    botónEliminar.setPreferredSize(dimBoton);

    panelBotones.add(botónAgregar);
    panelBotones.add(botónActualizar);
    panelBotones.add(botónServicios);
    panelBotones.add(botónEliminar);

    add(panelBotones, BorderLayout.NORTH);

    // Columnas con nombres claros
    String[] columnas = {
      "ID (Cédula/RUC)", "Tipo", "Nombre / Razón Social", "Servicio Ofrecido",
      "Celular", "Correo Electrónico", "Dirección", "Ult. Modif.", "Estado"
    };

    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public Class<?> getColumnClass(int column) {
        return String.class;
      }
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tabla = new JTable(modelo);
    configurarEstiloTabla();

    JScrollPane panelDesplazamiento = new JScrollPane(tabla);
    panelDesplazamiento.setOpaque(false);
    panelDesplazamiento.getViewport().setBackground(new Color(31, 41, 55));
    panelDesplazamiento.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelDesplazamiento, BorderLayout.CENTER);

    botónAgregar.addActionListener(e -> abrirDialogoRegistrar());
    botónActualizar.addActionListener(e -> abrirDialogoActualizar());
    botónServicios.addActionListener(e -> abrirDialogoServicios());
    botónEliminar.addActionListener(e -> eliminar());
  }

  private void configurarEstiloTabla() {
    tabla.setBackground(new Color(31, 41, 55));
    tabla.setForeground(Color.WHITE);
    tabla.setGridColor(new Color(55, 65, 81));
    tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tabla.setRowHeight(30);
    tabla.setSelectionBackground(new Color(75, 85, 99));
    tabla.setSelectionForeground(Color.WHITE);

    // Ajustar anchos para que se lea bien el servicio y el correo
    tabla.getColumnModel().getColumn(3).setPreferredWidth(180); // Servicio
    tabla.getColumnModel().getColumn(5).setPreferredWidth(150); // Email

    JTableHeader header = tabla.getTableHeader();
    header.setBackground(new Color(243, 244, 246));
    header.setForeground(new Color(31, 41, 55));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    cellRenderer.setBackground(new Color(31, 41, 55));
    cellRenderer.setForeground(Color.WHITE);

    for (int i = 0; i < tabla.getColumnCount(); i++) {
      tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }
  }

  private void abrirDialogoRegistrar() {
    JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Registrar Nuevo Proveedor", Dialog.ModalityType.APPLICATION_MODAL);
    dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(new EmptyBorder(15, 15, 15, 15));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(6, 6, 6, 6);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JRadioButton rbPersona = new JRadioButton("Persona Natural", true);
    JRadioButton rbEmpresa = new JRadioButton("Empresa / Sociedad");
    ButtonGroup groupTipo = new ButtonGroup();
    groupTipo.add(rbPersona); groupTipo.add(rbEmpresa);

    JTextField txtID = new JTextField(20);
    JTextField txtNombre = new JTextField(20);
    JTextField txtApellido = new JTextField(20);
    JComboBox<String> cmbCategoria = new JComboBox<>(CATALOGO_SERVICIOS); // Uso del catálogo amigable
    JTextField txtCelular = new JTextField(20);
    JTextField txtEmail = new JTextField(20);
    JTextField txtDireccion = new JTextField(20);

    JLabel lblID = new JLabel("Cédula (10 dígitos):");
    JLabel lblNombre = new JLabel("Nombres:");
    JLabel lblApellido = new JLabel("Apellidos:");

    // Lógica visual para cambiar etiquetas según selección
    rbPersona.addItemListener(e -> {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        lblID.setText("Cédula (10 dígitos):");
        lblNombre.setText("Nombres:");
        lblApellido.setVisible(true); txtApellido.setVisible(true);
      }
    });
    rbEmpresa.addItemListener(e -> {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        lblID.setText("RUC (13 dígitos):");
        lblNombre.setText("Razón Social:");
        lblApellido.setVisible(false); txtApellido.setVisible(false);
      }
    });

    int row = 0;
    gbc.gridx=0; gbc.gridy=row; panel.add(new JLabel("¿Qué tipo de proveedor es?"), gbc);
    JPanel pRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    pRadio.add(rbPersona); pRadio.add(rbEmpresa);
    gbc.gridx=1; panel.add(pRadio, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(lblID, gbc);
    gbc.gridx=1; panel.add(txtID, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(lblNombre, gbc);
    gbc.gridx=1; panel.add(txtNombre, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(lblApellido, gbc);
    gbc.gridx=1; panel.add(txtApellido, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(new JLabel("Tipo de Servicio:"), gbc);
    gbc.gridx=1; panel.add(cmbCategoria, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(new JLabel("Celular (09XXXXXXXX):"), gbc);
    gbc.gridx=1; panel.add(txtCelular, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(new JLabel("Correo Electrónico (usuario@gmail.com):"), gbc);
    gbc.gridx=1; panel.add(txtEmail, gbc);

    row++; gbc.gridx=0; gbc.gridy=row; panel.add(new JLabel("Dirección:"), gbc);
    gbc.gridx=1; panel.add(txtDireccion, gbc);

    Botón btnGuardar = new Botón("Guardar Proveedor", new Color(40, 167, 69));

    btnGuardar.addActionListener(ev -> {
      boolean esEmpresa = rbEmpresa.isSelected();
      String id = txtID.getText().trim();
      String nombre = txtNombre.getText().trim();
      String apellido = txtApellido.getText().trim();
      String celular = txtCelular.getText().trim();
      String email = txtEmail.getText().trim();
      String direccion = txtDireccion.getText().trim();

      // 1. Campos Vacíos
      if (id.isEmpty() || nombre.isEmpty() || celular.isEmpty() || email.isEmpty() || direccion.isEmpty()) {
        mostrarError("Por favor, llene todos los campos obligatorios."); return;
      }
      if (!esEmpresa && apellido.isEmpty()) {
        mostrarError("Debe ingresar el apellido para Personas Naturales."); return;
      }

      // 2. Validación Anexo 1
      String errorDoc = validarDocumentoEcuador(id, esEmpresa);
      if (errorDoc != null) {
        mostrarError("Error en identificación: " + errorDoc); return;
      }

      // 3. Validación Celular
      if (!celular.matches("09\\d{8}")) {
        mostrarError("Número de teléfono debe empezar con 09 y tener 10 dígitos exactos."); return;
      }

      // 4. Validación Email Estricta
      if (!email.matches(REGEX_EMAIL)) {
        mostrarError("Formato de correo inválido.\nDebe ser: usuario@dominio.extensión (ej: .com, .ec)"); return;
      }

      // 5. Longitud de textos
      if (nombre.length() > 100 || direccion.length() > 200) {
        mostrarError("El nombre o la dirección son demasiado largos."); return;
      }

      String nombreFinal = esEmpresa ? nombre : nombre + " " + apellido;
      modelo.addRow(new Object[]{
        id,
        esEmpresa ? "Empresa" : "Persona",
        nombreFinal,
        cmbCategoria.getSelectedItem().toString(),
        celular,
        email,
        direccion,
        new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
        "Activo" // Guardamos como texto para que sea claro
      });

      historialServicios.put(id, new ArrayList<>());
      historialReclamos.put(id, new ArrayList<>());

      JOptionPane.showMessageDialog(dialogo, "¡Proveedor registrado exitosamente!");
      dialogo.dispose();
    });

    dialogo.add(panel, BorderLayout.CENTER);
    JPanel pBtn = new JPanel(); pBtn.add(btnGuardar);
    dialogo.add(pBtn, BorderLayout.SOUTH);
    dialogo.pack();
    dialogo.setLocationRelativeTo(this);
    dialogo.setVisible(true);
  }

  private void abrirDialogoActualizar() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      JOptionPane.showMessageDialog(this, "Primero seleccione un proveedor de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }

    String estadoActualTexto = modelo.getValueAt(fila, 8).toString();
    boolean estaActivo = estadoActualTexto.equals("Activo");

    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Actualizar Información de Contacto", Dialog.ModalityType.APPLICATION_MODAL);
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); gbc.anchor = GridBagConstraints.WEST;

    JTextField txtCelular = new JTextField(modelo.getValueAt(fila, 4).toString(), 20);
    JTextField txtEmail = new JTextField(modelo.getValueAt(fila, 5).toString(), 20);
    JTextField txtDireccion = new JTextField(modelo.getValueAt(fila, 6).toString(), 20);
    JCheckBox chkActivo = new JCheckBox("Proveedor Activo (Habilitado)", estaActivo);

    if (!estaActivo) {
      txtCelular.setEnabled(false);
      txtEmail.setEnabled(false);
      txtDireccion.setEnabled(false);
      JLabel advertencia = new JLabel("<html><font color='red'><b>Proveedor Inactivo</b><br>Active la casilla para poder editar los datos.</font></html>");
      gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; panel.add(advertencia, gbc);
      gbc.gridwidth=1;
    }

    int y = (!estaActivo) ? 1 : 0; // Ajustar posición si hay advertencia
    gbc.gridx=0; gbc.gridy=y++; panel.add(new JLabel("Celular:"), gbc);
    gbc.gridx=1; panel.add(txtCelular, gbc);

    gbc.gridx=0; gbc.gridy=y++; panel.add(new JLabel("Correo Electrónico:"), gbc);
    gbc.gridx=1; panel.add(txtEmail, gbc);

    gbc.gridx=0; gbc.gridy=y++; panel.add(new JLabel("Dirección:"), gbc);
    gbc.gridx=1; panel.add(txtDireccion, gbc);

    gbc.gridx=0; gbc.gridy=y++; panel.add(chkActivo, gbc);

    Botón btnActualizar = new Botón("Guardar Cambios", new Color(234, 177, 0));

    btnActualizar.addActionListener(e -> {
      // Validar solo si los campos están habilitados
      if (txtCelular.isEnabled()) {
        if (!txtCelular.getText().matches("09\\d{8}")) {
          mostrarError("El celular debe tener 10 dígitos (empezar con 0998929489)."); return;
        }
        if (!txtEmail.getText().matches(REGEX_EMAIL)) {
          mostrarError("Formato de correo inválido (ej: nombre@dominio.com)."); return;
        }
      }

      modelo.setValueAt(txtCelular.getText(), fila, 4);
      modelo.setValueAt(txtEmail.getText(), fila, 5);
      modelo.setValueAt(txtDireccion.getText(), fila, 6);
      modelo.setValueAt(chkActivo.isSelected() ? "Activo" : "Inactivo", fila, 8);

      // Registrar fecha de modificación solo si estaba activo y se modificó algo
      if (estaActivo) {
        modelo.setValueAt(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), fila, 7);
      }

      JOptionPane.showMessageDialog(dialog, "Datos actualizados correctamente.");
      dialog.dispose();
    });

    dialog.add(panel, BorderLayout.CENTER);
    JPanel pBtn = new JPanel(); pBtn.add(btnActualizar);
    dialog.add(pBtn, BorderLayout.SOUTH);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void abrirDialogoServicios() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      JOptionPane.showMessageDialog(this, "Seleccione un proveedor para gestionar sus servicios.", "Aviso", JOptionPane.WARNING_MESSAGE);
      return;
    }

    String idProveedor = modelo.getValueAt(fila, 0).toString();
    String nombreProv = modelo.getValueAt(fila, 2).toString();

    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Gestión: " + nombreProv, Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setSize(650, 500);
    dialog.setLayout(new BorderLayout());

    JTabbedPane tabs = new JTabbedPane();

    // --- PESTAÑA SERVICIOS ---
    JPanel panelServicios = new JPanel(new BorderLayout());
    DefaultListModel<String> listModelServ = new DefaultListModel<>();
    if (historialServicios.containsKey(idProveedor)) {
      historialServicios.get(idProveedor).forEach(listModelServ::addElement);
    }

    JPanel panelAddServ = new JPanel(new GridLayout(5, 2, 5, 5)); // Espaciado mejorado
    panelAddServ.setBorder(new EmptyBorder(10,10,10,10));

    JTextField txtFechaInicio = new JTextField("dd/mm/aaaa");
    JTextField txtFechaFin = new JTextField("dd/mm/aaaa");
    JTextField txtObs = new JTextField();
    JSpinner spinCalif = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));

    panelAddServ.add(new JLabel("Fecha Inicio (dd/mm/aaaa):")); panelAddServ.add(txtFechaInicio);
    panelAddServ.add(new JLabel("Fecha Fin (dd/mm/aaaa):")); panelAddServ.add(txtFechaFin);
    panelAddServ.add(new JLabel("Observaciones / Detalles:")); panelAddServ.add(txtObs);
    panelAddServ.add(new JLabel("Calificación (1=Malo, 5=Excelente):")); panelAddServ.add(spinCalif);
    panelAddServ.add(new JLabel("")); // Espacio vacío

    Botón btnAddServ = new Botón("Registrar Mantenimiento", new Color(40, 167, 69));
    panelAddServ.add(btnAddServ);

    btnAddServ.addActionListener(e -> {
      if (!txtFechaInicio.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
        mostrarError("La fecha debe tener formato dd/mm/aaaa (ej: 01/05/2024)"); return;
      }
      String registro = String.format("• Servicio: %s al %s | Calif: %s/5 | Nota: %s",
        txtFechaInicio.getText(), txtFechaFin.getText(), spinCalif.getValue(), txtObs.getText());

      if (!historialServicios.containsKey(idProveedor)) historialServicios.put(idProveedor, new ArrayList<>());
      historialServicios.get(idProveedor).add(registro);
      listModelServ.addElement(registro);
    });

    panelServicios.add(new JScrollPane(new JList<>(listModelServ)), BorderLayout.CENTER);
    panelServicios.add(panelAddServ, BorderLayout.SOUTH);

    // --- PESTAÑA RECLAMOS ---
    JPanel panelReclamos = new JPanel(new BorderLayout());
    DefaultListModel<String> listModelRec = new DefaultListModel<>();
    if (historialReclamos.containsKey(idProveedor)) {
      historialReclamos.get(idProveedor).forEach(listModelRec::addElement);
    }

    JPanel panelAddRec = new JPanel(new BorderLayout());
    JTextField txtReclamo = new JTextField();
    Botón btnAddRec = new Botón("Registrar Reclamo", new Color(220, 53, 69));

    panelAddRec.add(new JLabel("  Detalle del problema: "), BorderLayout.WEST);
    panelAddRec.add(txtReclamo, BorderLayout.CENTER);
    panelAddRec.add(btnAddRec, BorderLayout.EAST);

    btnAddRec.addActionListener(e -> {
      if (txtReclamo.getText().isEmpty()) return;
      String rec = "• [" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "] RECLAMO: " + txtReclamo.getText();
      if (!historialReclamos.containsKey(idProveedor)) historialReclamos.put(idProveedor, new ArrayList<>());
      historialReclamos.get(idProveedor).add(rec);
      listModelRec.addElement(rec);
      txtReclamo.setText("");
      JOptionPane.showMessageDialog(dialog, "Reclamo registrado correctamente.");
    });

    panelReclamos.add(new JScrollPane(new JList<>(listModelRec)), BorderLayout.CENTER);
    panelReclamos.add(panelAddRec, BorderLayout.SOUTH);

    tabs.addTab("Historial de Mantenimientos", panelServicios);
    tabs.addTab("Reclamos", panelReclamos);

    dialog.add(tabs);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void eliminar() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      mostrarError("Seleccione un proveedor para eliminar."); return;
    }
    JPasswordField pass = new JPasswordField();
    if (JOptionPane.showConfirmDialog(this, pass, "Ingrese Contraseña de Administrador", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      if (ADMIN_PASSWORD.equals(new String(pass.getPassword()))) {
        String id = modelo.getValueAt(fila, 0).toString();
        modelo.removeRow(fila);
        historialServicios.remove(id);
        historialReclamos.remove(id);
        JOptionPane.showMessageDialog(this, "Proveedor eliminado del sistema.");
      } else {
        mostrarError("Contraseña incorrecta. Acción denegada.");
      }
    }
  }

  private void mostrarError(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Error de Validación", JOptionPane.ERROR_MESSAGE);
  }


  private String validarDocumentoEcuador(String documento, boolean esRuc) {
    if (documento == null || !documento.matches("\\d+")) return "Solo se permiten números.";

    int len = documento.length();
    if (esRuc && len != 13) return "El RUC debe tener 13 dígitos.";
    if (!esRuc && len != 10) return "La cédula debe tener 10 dígitos.";
    if (esRuc && !documento.endsWith("001")) return "El RUC debe terminar en 001.";

    // Validación de Provincia
    int provincia = Integer.parseInt(documento.substring(0, 2));
    if (provincia < 1 || provincia > 24) return "Código de provincia inválido.";

    // Validación 3er dígito (Tipo de entidad)
    int tercerDigito = Integer.parseInt(documento.substring(2, 3));

    if (!esRuc) {
      if (tercerDigito >= 6) return "Cédula inválida.";
      return null;
    } else {
      // Reglas Anexo 1: 9 (Privada), 6 (Pública), <6 (Natural)
      if (tercerDigito == 9) return null;
      if (tercerDigito == 6) return null;
      if (tercerDigito < 6) return null;
      return "Tercer dígito de RUC inválido para el tipo de empresa.";
    }
  }
}
