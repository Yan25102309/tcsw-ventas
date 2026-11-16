#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Generador del Documento Word (.docx) para la Sección IV: Reporte de Trazabilidad - Victor Reyes
Práctica P10 (Cliente Angular) - Módulo M13 - TCSW-19234
Equipo E05 - Integrante 1: Victor Gustavo Reyes Vidal (Novaxll)

Replica de forma exacta la tipografía, paleta cromática, geometría y estructura
estandarizada en las entregas del equipo (P09_EQUIPO_E05.pdf), e incrusta las
capturas reales de evidencia de las Figuras 1 a 6.
"""

import os
import shutil
from docx import Document
from docx.shared import Inches, Pt, RGBColor, Mm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

# --- CONSTANTES DE COLOR Y ESTILO ---
COLOR_NAVY_HEADER = "1F4E78"        # Fondo de cabeceras de tablas Ghostty y Review
COLOR_NAVY_BULLET = RGBColor(0x1F, 0x4E, 0x79) # Títulos de archivos en viñetas
COLOR_HEADING_STEEL = RGBColor(0x36, 0x5F, 0x91) # Heading 1 (1. Desarrollo, 2. Review, 3. Verif)
COLOR_TEXT_BODY = RGBColor(0x22, 0x22, 0x22)    # Texto normal párrafos
COLOR_CAPTION = RGBColor(0x33, 0x33, 0x33)      # Rótulos de figuras
COLOR_MUTED = RGBColor(0x77, 0x77, 0x77)        # Texto guía en placeholders
COLOR_WHITE = RGBColor(0xFF, 0xFF, 0xFF)        # Texto blanco sobre cabeceras navy
COLOR_ZEBRA = "F4F5F9"                          # Fila alterna en tablas y label de metadatos
COLOR_BORDER_METADATA = "B0C3DE"                # Borde tabla metadatos
COLOR_BORDER_GENERAL = "D3D3D3"                 # Borde tablas generales

def apply_font(run, name="Arial", size_pt=10.5, bold=False, italic=False, color_rgb=None):
    """Garantiza la tipografía Arial en Word en todos los motores de renderizado."""
    run.font.name = name
    run.font.size = Pt(size_pt)
    run.bold = bold
    run.italic = italic
    if color_rgb:
        run.font.color.rgb = color_rgb
    
    rPr = run._element.get_or_add_rPr()
    rFonts = parse_xml(f'<w:rFonts {nsdecls("w")} w:ascii="{name}" w:hAnsi="{name}" w:cs="{name}"/>')
    rPr.append(rFonts)

def set_cell_background(cell, hex_color):
    """Aplica sombreado de celda con w:shd."""
    shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>')
    cell._tc.get_or_add_tcPr().append(shading_elm)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    """Configura el padding interior de la celda en dxa."""
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(
        f'<w:tcMar {nsdecls("w")}>'
        f'<w:top w:w="{top}" w:type="dxa"/>'
        f'<w:bottom w:w="{bottom}" w:type="dxa"/>'
        f'<w:left w:w="{left}" w:type="dxa"/>'
        f'<w:right w:w="{right}" w:type="dxa"/>'
        f'</w:tcMar>'
    )
    tcPr.append(tcMar)

def set_table_borders(table, color="D3D3D3", sz="6", val="single"):
    """Aplica bordes finos de tabla con w:tblBorders."""
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        f'<w:top w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:left w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:bottom w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:right w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:insideH w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:insideV w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'</w:tblBorders>'
    )
    tblPr.append(borders)

def set_row_properties(row, is_header=False):
    """Evita división de filas entre páginas y repite encabezado si hay salto."""
    trPr = row._tr.get_or_add_trPr()
    trPr.append(parse_xml(f'<w:cantSplit {nsdecls("w")}/>'))
    if is_header:
        trPr.append(parse_xml(f'<w:tblHeader {nsdecls("w")}/>'))

def add_paragraph_styled(doc, text="", space_before=0, space_after=6, line_spacing=1.25, align=WD_ALIGN_PARAGRAPH.LEFT):
    """Crea un párrafo estándar con espaciados precisos."""
    p = doc.add_paragraph()
    p.alignment = align
    p.paragraph_format.space_before = Pt(space_before)
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.line_spacing = line_spacing
    if text:
        r = p.add_run(text)
        apply_font(r, name="Arial", size_pt=10.5, color_rgb=COLOR_TEXT_BODY)
    return p

def add_heading_1(doc, text):
    """Crea un encabezado de nivel 1 con el estilo exacto de P09 (#365F91, Arial Bold 12pt)."""
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(18)
    p.paragraph_format.space_after = Pt(6)
    p.paragraph_format.line_spacing = 1.25
    p.paragraph_format.keep_with_next = True
    r = p.add_run(text)
    apply_font(r, name="Arial", size_pt=12.0, bold=True, color_rgb=COLOR_HEADING_STEEL)
    return p

def add_artifact_bullet(doc, title, description):
    """Crea una viñeta bicolor: archivo en Navy Bold #1F4E79 y descripción en negro regular."""
    p = doc.add_paragraph(style='List Bullet')
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(4)
    p.paragraph_format.line_spacing = 1.25
    
    r_title = p.add_run(title)
    apply_font(r_title, name="Arial", size_pt=11.0, bold=True, color_rgb=COLOR_NAVY_BULLET)
    
    r_desc = p.add_run(" " + description)
    apply_font(r_desc, name="Arial", size_pt=11.0, bold=False, color_rgb=RGBColor(0x00, 0x00, 0x00))
    return p

def add_figure(doc, fig_number, caption_text, image_path=None, width_in=6.27):
    """Inserta rótulo de figura ARRIBA e incrusta la imagen limpia centrada (o placeholder si no existe)."""
    p_cap = doc.add_paragraph()
    p_cap.paragraph_format.space_before = Pt(14)
    p_cap.paragraph_format.space_after = Pt(4)
    p_cap.paragraph_format.keep_with_next = True
    r_cap = p_cap.add_run(f"Figura {fig_number}. {caption_text}")
    apply_font(r_cap, name="Arial", size_pt=10.0, bold=True, color_rgb=COLOR_CAPTION)
    
    if image_path and os.path.exists(image_path):
        p_img = doc.add_paragraph()
        p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_img.paragraph_format.space_before = Pt(4)
        p_img.paragraph_format.space_after = Pt(12)
        r_img = p_img.add_run()
        r_img.add_picture(image_path, width=Inches(width_in))
    else:
        tbl = doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        set_table_borders(tbl, color=COLOR_BORDER_GENERAL, sz="6", val="dashed")
        cell = tbl.cell(0, 0)
        cell.width = Inches(width_in)
        set_cell_background(cell, "FAFAFA")
        set_cell_margins(cell, top=240, bottom=240, left=200, right=200)
        p_box = cell.paragraphs[0]
        p_box.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_box.paragraph_format.space_before = Pt(0)
        p_box.paragraph_format.space_after = Pt(0)
        r_box = p_box.add_run(f"[ESPACIO PARA CAPTURA DE PANTALLA: Figura {fig_number}]\n({caption_text})")
        apply_font(r_box, name="Arial", size_pt=9.5, italic=True, color_rgb=COLOR_MUTED)

def create_document():
    doc = Document()
    
    # --- CONFIGURACIÓN DE PÁGINA (A4, márgenes 1 in) ---
    section = doc.sections[0]
    section.page_width = Mm(210)
    section.page_height = Mm(297)
    section.top_margin = Inches(1.0)
    section.bottom_margin = Inches(1.0)
    section.left_margin = Inches(1.0)
    section.right_margin = Inches(1.0)
    
    header = section.header
    header.is_linked_to_previous = False
    for p in header.paragraphs:
        p.text = ""
    footer = section.footer
    footer.is_linked_to_previous = False
    for p in footer.paragraphs:
        p.text = ""
        
    # --- TÍTULO DE SECCIÓN IV ---
    p_title1 = doc.add_paragraph()
    p_title1.paragraph_format.space_before = Pt(0)
    p_title1.paragraph_format.space_after = Pt(0)
    p_title1.paragraph_format.line_spacing = 1.15
    r_t1 = p_title1.add_run("Sección IV: Reporte de trazabilidad -")
    apply_font(r_t1, name="Arial", size_pt=23.0, bold=True, color_rgb=RGBColor(0x00, 0x00, 0x00))
    
    p_title2 = doc.add_paragraph()
    p_title2.paragraph_format.space_before = Pt(0)
    p_title2.paragraph_format.space_after = Pt(14)
    p_title2.paragraph_format.line_spacing = 1.15
    r_t2 = p_title2.add_run("Victor Gustavo Reyes Vidal")
    apply_font(r_t2, name="Arial", size_pt=23.0, bold=True, color_rgb=RGBColor(0x00, 0x00, 0x00))
    
    # --- TABLA 1: METADATOS DEL PR ---
    tbl_meta = doc.add_table(rows=2, cols=2)
    tbl_meta.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(tbl_meta, color=COLOR_BORDER_METADATA, sz="8", val="single")
    
    meta_data = [
        ("URL del último PR", "https://github.com/Yan25102309/tcsw-ventas/pull/64"),
        ("Último SHA-1 del PR", "1ca5a7454a08d5be198faefa98b495969404c63f")
    ]
    col_widths_meta = [Inches(1.95), Inches(4.32)]
    
    for row_idx, (label, value) in enumerate(meta_data):
        row = tbl_meta.rows[row_idx]
        set_row_properties(row)
        
        cell_lbl = row.cells[0]
        cell_lbl.width = col_widths_meta[0]
        set_cell_background(cell_lbl, COLOR_ZEBRA)
        set_cell_margins(cell_lbl, top=100, bottom=100, left=150, right=150)
        p_l = cell_lbl.paragraphs[0]
        p_l.paragraph_format.space_before = Pt(0)
        p_l.paragraph_format.space_after = Pt(0)
        r_l = p_l.add_run(label)
        apply_font(r_l, name="Arial", size_pt=10.5, bold=True, color_rgb=COLOR_TEXT_BODY)
        
        cell_val = row.cells[1]
        cell_val.width = col_widths_meta[1]
        set_cell_background(cell_val, "FFFFFF")
        set_cell_margins(cell_val, top=100, bottom=100, left=150, right=150)
        p_v = cell_val.paragraphs[0]
        p_v.paragraph_format.space_before = Pt(0)
        p_v.paragraph_format.space_after = Pt(0)
        r_v = p_v.add_run(value)
        apply_font(r_v, name="Arial", size_pt=10.5, bold=False, color_rgb=COLOR_TEXT_BODY)

    # --- SUBSECCIÓN 1: DESARROLLO E IMPLEMENTACIÓN DEL ISSUE #62 ---
    add_heading_1(doc, "1. Desarrollo e Implementación del Issue #62 (Configuración de Angular 16, DTOs y Modelos, ProductosService y ProductosPage — PR #64)")
    
    p_intro1 = add_paragraph_styled(doc, space_after=8)
    r_intro1 = p_intro1.add_run(
        "Para dar cumplimiento a mi responsabilidad técnica en el incremento P10 (Módulo M13 — Angular Básico), "
        "me asigné el Issue #62 (\"feat(frontend): configuracion de Angular, DTOs, ProductosService y ProductosPage\"). "
        "En la rama de trabajo aislada feat/p10-angular-servicio-productos-listado configuré la arquitectura "
        "base del cliente frontend web en Angular 16, definiendo los contratos de datos desacoplados de la "
        "API REST, encapsulando la infraestructura de red mediante un servicio inyectable y construyendo las "
        "vistas de consulta reactivas con soporte explícito para la máquina de estados asíncronos:"
    )
    apply_font(r_intro1, name="Arial", size_pt=10.5, color_rgb=COLOR_TEXT_BODY)
    
    add_artifact_bullet(doc, "producto.model.ts:",
        "Objetos de Transferencia de Datos (DTOs) e interfaces TypeScript que modelan el contrato formal expuesto "
        "por la API en OpenAPI 3.1.0. Define Producto (id, nombre, precio, existencia, activo, categoria) y "
        "CrearProductoRequest (cuerpo tipado para altas sin identificadores autogenerados).")
    
    add_artifact_bullet(doc, "ui-state.model.ts:",
        "Tipo discriminado que modela de forma determinista el ciclo de estados de la interfaz de usuario: "
        "'idle' | 'loading' | 'success' | 'empty' | 'error'. Previene estados indeterminados en la vista y "
        "gobierna el renderizado condicional exhaustivo.")
    
    add_artifact_bullet(doc, "productos.service.ts:",
        "Servicio inyectable (@Injectable({ providedIn: 'root' })) que encapsula el consumo de la API REST /api/v1/productos "
        "mediante HttpClient. Proporciona los métodos reactivos obtenerTodos(): Observable<Producto[]> y "
        "obtenerPorId(id: number): Observable<Producto>, canalizando los fallos de red con catchError y soporte "
        "para el estándar Problem Details (RFC 9457).")
    
    add_artifact_bullet(doc, "producto-list.component.ts (.html y .css):",
        "Componente de presentación puro (Dumb Component) con selector app-producto-list. Renderiza una tabla HTML "
        "semántica y accesible (atributos WAI-ARIA scope=\"col\", aria-label=\"Catálogo de productos\"), formateo de "
        "importes mediante currency:'MXN' y distintivos visuales de inventario (badge-success para existencias activas "
        "y badge-danger para agotados).")
    
    add_artifact_bullet(doc, "productos-page.component.ts (.html y .css):",
        "Componente coordinador inteligente (Smart Container) con selector app-productos-page. Administra el ciclo de vida "
        "de la petición invocando al servicio en ngOnInit, transiciona reactivamente entre los estados de ui-state.model.ts "
        "y ofrece un mecanismo interactivo de recuperación defensiva ante fallos de conexión mediante el botón \"Reintentar\".")
    
    add_artifact_bullet(doc, "productos.service.spec.ts:",
        "Suite de pruebas unitarias aisladas con TestBed y dobles de red HttpTestingController. Comprueba la emisión correcta "
        "de peticiones GET, el retorno del arreglo tipado, la respuesta ante catálogos vacíos y la propagación adecuada de "
        "errores HTTP (status 500 y fallo de red status 0).")
    
    add_artifact_bullet(doc, "producto-list.component.spec.ts:",
        "Suite de pruebas unitarias que evalúa el renderizado en el DOM de la tabla de productos, el enlace unidireccional "
        "de datos (@Input) y el formateo monetario de los precios.")
    
    add_artifact_bullet(doc, "productos-page.component.spec.ts:",
        "Suite de pruebas unitarias que evalúa las transiciones del estado asíncrono UI ('loading' -> 'success', 'loading' -> "
        "'empty' y 'loading' -> 'error') y la re-ejecución del servicio tras accionar el botón de reintento en la vista.")
    
    add_artifact_bullet(doc, "Configuración de Infraestructura y Tooling (proxy.conf.json, karma.conf.js, angular.json):",
        "Configuración de Angular CLI 16.2.0, soporte headless en Karma sobre Chromium, y proxy inverso redirigiendo "
        "llamadas locales /api hacia http://localhost:8080 para evadir restricciones de CORS en desarrollo.")

    # Figura 1: Issue #62 (Incrusta captura real extraída)
    add_figure(doc, 1, "Issue #62 registrado y asignado a Victor Reyes en GitHub",
               image_path="docs/evidencias/figura1_issue62.png", width_in=6.27)
    
    # Subtítulo bitácora Ghostty
    p_sub_ghostty = doc.add_paragraph()
    p_sub_ghostty.paragraph_format.space_before = Pt(14)
    p_sub_ghostty.paragraph_format.space_after = Pt(6)
    p_sub_ghostty.paragraph_format.keep_with_next = True
    r_sg = p_sub_ghostty.add_run("Bitácora de Comandos Git, npm y Angular Ejecutados en Terminal Ghostty (Issue #62):")
    apply_font(r_sg, name="Arial", size_pt=10.5, bold=True, color_rgb=COLOR_TEXT_BODY)
    
    # TABLA 2: COMANDOS GHOSTTY
    tbl_ghostty = doc.add_table(rows=1, cols=2)
    tbl_ghostty.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(tbl_ghostty, color=COLOR_BORDER_GENERAL, sz="6", val="single")
    
    ghostty_widths = [Inches(3.47), Inches(2.80)]
    
    hdr_row = tbl_ghostty.rows[0]
    set_row_properties(hdr_row, is_header=True)
    hdr_titles = ["Comando Ejecutado (Ghostty)", "Propósito Técnico en el Flujo"]
    for i, title in enumerate(hdr_titles):
        c = hdr_row.cells[i]
        c.width = ghostty_widths[i]
        set_cell_background(c, COLOR_NAVY_HEADER)
        set_cell_margins(c, top=120, bottom=120, left=150, right=150)
        p = c.paragraphs[0]
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(0)
        r = p.add_run(title)
        apply_font(r, name="Arial", size_pt=10.5, bold=True, color_rgb=COLOR_WHITE)
        
    ghostty_data = [
        ("git checkout -b feat/p10-angular-servicio-productos-listado", "Crear y posicionarse en la rama de trabajo aislada para el desarrollo de la arquitectura Angular y el listado de productos."),
        ("cat << 'EOF' > frontend-angular/src/app/models/producto.model.ts", "Definir los DTOs e interfaces tipadas Producto y CrearProductoRequest alineados con OpenAPI 3.1.0."),
        ("cat << 'EOF' > frontend-angular/src/app/models/ui-state.model.ts", "Modelar el tipo discriminado EstadoUi para gobernar los estados asíncronos (idle, loading, success, empty, error)."),
        ("cat << 'EOF' > frontend-angular/src/app/services/productos.service.ts", "Implementar el servicio inyectable con HttpClient, encapsulando operaciones GET a /api/v1/productos y soporte RFC 9457."),
        ("cat << 'EOF' > frontend-angular/src/app/components/producto-list/producto-list.component.ts", "Crear el componente de presentación puro (Dumb) con tabla accesible WAI-ARIA, pipe de moneda MXN y distintivos de inventario."),
        ("cat << 'EOF' > frontend-angular/src/app/components/productos-page/productos-page.component.ts", "Construir el componente contenedor inteligente (Smart) con máquina de estados asíncronos y botón de reintento."),
        ("cat << 'EOF' > frontend-angular/src/app/services/productos.service.spec.ts", "Codificar pruebas unitarias con dobles HttpTestingController evaluando respuestas exitosas, catálogo vacío y fallos 500/0."),
        ("cat << 'EOF' > frontend-angular/src/app/components/producto-list/producto-list.component.spec.ts", "Implementar pruebas unitarias de renderizado DOM, enlace unidireccional de datos @Input y formateo de precios."),
        ("cat << 'EOF' > frontend-angular/src/app/components/productos-page/productos-page.component.spec.ts", "Crear pruebas de ciclo de vida del contenedor, transiciones entre estados UI y re-ejecución del servicio al reintentar."),
        ("npm test -- --watch=false", "Ejecutar la suite automatizada en Karma headless verificando 14 pruebas aprobadas con 100% de cobertura."),
        ("npm run build", "Compilar la aplicación para producción certificando la ausencia de errores en tipos TypeScript y empaquetado de bundles."),
        ("git push origin feat/p10-angular-servicio-productos-listado", "Publicar la rama remota en GitHub para habilitar la apertura formal de la Pull Request #64 cerrando el Issue #62.")
    ]
    
    for row_idx, (cmd, purp) in enumerate(ghostty_data):
        row = tbl_ghostty.add_row()
        set_row_properties(row)
        bg = COLOR_ZEBRA if row_idx % 2 == 1 else "FFFFFF"
        
        c1 = row.cells[0]
        c1.width = ghostty_widths[0]
        set_cell_background(c1, bg)
        set_cell_margins(c1, top=80, bottom=80, left=150, right=150)
        p1 = c1.paragraphs[0]
        p1.paragraph_format.space_before = Pt(0)
        p1.paragraph_format.space_after = Pt(0)
        r1 = p1.add_run(cmd)
        apply_font(r1, name="Arial", size_pt=9.5, color_rgb=COLOR_TEXT_BODY)
        
        c2 = row.cells[1]
        c2.width = ghostty_widths[1]
        set_cell_background(c2, bg)
        set_cell_margins(c2, top=80, bottom=80, left=150, right=150)
        p2 = c2.paragraphs[0]
        p2.paragraph_format.space_before = Pt(0)
        p2.paragraph_format.space_after = Pt(0)
        r2 = p2.add_run(purp)
        apply_font(r2, name="Arial", size_pt=9.5, color_rgb=COLOR_TEXT_BODY)

    # Figura 2: Tests Karma (Incrusta captura real extraída)
    add_figure(doc, 2, "Ejecución exitosa de 'npm test -- --watch=false' (14 pruebas unitarias aprobadas en verde con 100% de cobertura)",
               image_path="docs/evidencias/figura2_tests_karma.png", width_in=6.27)
    
    # Figura 3: PR #64 Merged (Incrusta captura real extraída)
    add_figure(doc, 3, "Pull Request #64 abierta y fusionada en GitHub cerrando el Issue #62",
               image_path="docs/evidencias/figura3_pr64_merged.png", width_in=6.27)

    # --- SUBSECCIÓN 2: EVALUACIÓN Y REVISIÓN DE CÓDIGO (ANA MARÍA) ---
    add_heading_1(doc, "2. Evaluación y Revisión (Code Review) de la Pull Request #66 de Ana María (Issue #65 — Formulario Reactivo, Rutas y Reporte P10)")
    
    p_intro2 = add_paragraph_styled(doc, space_after=8)
    r_intro2 = p_intro2.add_run(
        "En estricto apego a las directrices de gobernanza de software y la prohibición expresa de autoaprobación "
        "de código, actué como revisor técnico (Code Reviewer) para los incrementos desarrollados por mi compañera "
        "Ana María Reyes Hernández (Yan25102309) en el marco del Issue #65 y su Pull Request #66. A continuación "
        "se documenta la auditoría exhaustiva realizada sobre el formulario reactivo ProductoForm, el módulo de rutas "
        "app.routes.ts, el bootstrapeo de la aplicación y el reporte integrador:"
    )
    apply_font(r_intro2, name="Arial", size_pt=10.5, color_rgb=COLOR_TEXT_BODY)
    
    # TABLA 3: CODE REVIEW
    tbl_review = doc.add_table(rows=1, cols=4)
    tbl_review.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(tbl_review, color=COLOR_BORDER_GENERAL, sz="6", val="single")
    
    review_widths = [Inches(1.97), Inches(1.01), Inches(1.12), Inches(2.17)]
    
    hdr_review = tbl_review.rows[0]
    set_row_properties(hdr_review, is_header=True)
    hdr_rev_titles = ["Archivo Evaluado", "Línea / Ubicación", "Clasificación", "Comentario Sustantivo Registrado en GitHub"]
    for i, title in enumerate(hdr_rev_titles):
        c = hdr_review.cells[i]
        c.width = review_widths[i]
        set_cell_background(c, COLOR_NAVY_HEADER)
        set_cell_margins(c, top=120, bottom=120, left=120, right=120)
        p = c.paragraphs[0]
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(0)
        r = p.add_run(title)
        apply_font(r, name="Arial", size_pt=10.5, bold=True, color_rgb=COLOR_WHITE)
        
    review_data = [
        ("src/app/components/producto-form/producto-form.component.ts", "L. 15-55", "[Revisión]", "Validación estricta con ReactiveFormsModule (FormGroup y FormControl). Control exhaustivo de rangos numéricos (>0) y mensajes declarativos."),
        ("src/app/components/producto-form/producto-form.component.html", "L. 1-45", "[Accesibilidad]", "Cumplimiento WAI-ARIA en controles de entrada (aria-invalid, aria-describedby) y alertas accesibles con role=\"alert\"."),
        ("src/app/components/producto-form/producto-form.component.spec.ts", "L. 20-40", "[Validación]", "Pruebas unitarias que certifican la invalidación del formulario ante campos vacíos y la emisión tipada del evento de registro."),
        ("src/app/app.routes.ts", "L. 8-28", "[Validación]", "Configuración del enrutamiento respetando el principio de primera coincidencia: /productos y /productos/nuevo ordenados estrictamente antes del comodín (**)."),
        ("src/app/app.config.ts", "L. 1-15", "[Revisión]", "Configuración centralizada de proveedores con provideRouter y provideHttpClient, garantizando inyección limpia sin módulos obsoletos."),
        ("docs/P10_reporte_cliente_angular.md", "L. 1-63", "[Validación]", "Excelente documentación integradora con árbol de componentes, matriz formal de rutas y diagrama de interacción en Mermaid (sequenceDiagram).")
    ]
    
    for row_idx, (arch, linea, clasif, coment) in enumerate(review_data):
        row = tbl_review.add_row()
        set_row_properties(row)
        bg = COLOR_ZEBRA if row_idx % 2 == 1 else "FFFFFF"
        
        items = [arch, linea, clasif, coment]
        for c_idx, val in enumerate(items):
            c = row.cells[c_idx]
            c.width = review_widths[c_idx]
            set_cell_background(c, bg)
            set_cell_margins(c, top=80, bottom=80, left=120, right=120)
            p = c.paragraphs[0]
            p.paragraph_format.space_before = Pt(0)
            p.paragraph_format.space_after = Pt(0)
            r = p.add_run(val)
            apply_font(r, name="Arial", size_pt=9.0, color_rgb=COLOR_TEXT_BODY)

    # Figura 4: Dictamen Approve (Incrusta captura real de GitHub)
    add_figure(doc, 4, "Dictamen de aprobación formal (Approve) y comentarios registrados en la Pull Request #66 de Ana María",
               image_path="docs/evidencias/figura4_aprobacion_pr66.png", width_in=5.8)
    
    # Figura 5: Merge confirmado (Incrusta captura real de GitHub)
    add_figure(doc, 5, "Fusión confirmada (Merged) de la Pull Request #66 de Ana María hacia la rama main",
               image_path="docs/evidencias/figura5_merge_pr66.png", width_in=5.8)

    # --- SUBSECCIÓN 3: VERIFICACIÓN TÉCNICA Y REFLEXIÓN INDIVIDUAL ---
    add_heading_1(doc, "3. Verificación Técnica del Cliente Angular y Reflexión Individual")
    
    p_verif = add_paragraph_styled(doc, space_after=8)
    r_verif = p_verif.add_run(
        "La verificación formal del incremento se sustentó en la compilación determinista del código fuente "
        "TypeScript (npm run build) y la ejecución desasistida de la suite completa de pruebas unitarias "
        "(npm test -- --watch=false). Las 17 pruebas automatizadas integradas certifican el 100% de éxito en "
        "la suite de pruebas sin dependencia de un backend activo. Asimismo, se validó la integración local "
        "mediante el proxy inverso proxy.conf.json, confirmando la recepción de datos reales desde los endpoints "
        "REST expuestos por Spring Boot en /api/v1/productos."
    )
    apply_font(r_verif, name="Arial", size_pt=10.5, color_rgb=COLOR_TEXT_BODY)
    
    # Figura 6: Verificación y tests consolidados (Incrusta captura real de suite consolidada)
    add_figure(doc, 6, "Suite consolidada de pruebas unitarias y compilación TypeScript exitosa en terminal Ghostty",
               image_path="docs/evidencias/figura6_suite_consolidada.png", width_in=6.27)
    
    # Subtítulo Reflexión
    p_sub_refl = doc.add_paragraph()
    p_sub_refl.paragraph_format.space_before = Pt(18)
    p_sub_refl.paragraph_format.space_after = Pt(6)
    p_sub_refl.paragraph_format.keep_with_next = True
    r_sr = p_sub_refl.add_run("Reflexión Técnica y Colaborativa:")
    apply_font(r_sr, name="Arial", size_pt=12.0, bold=True, color_rgb=COLOR_HEADING_STEEL)
    
    p_refl1 = add_paragraph_styled(doc, space_after=6)
    r_rf1 = p_refl1.add_run(
        "El desarrollo de la práctica P10 (Módulo M13) consolidó la comprensión práctica sobre el desacoplamiento "
        "entre la interfaz visual y los servicios de infraestructura en una aplicación de frontend moderna. El "
        "mayor aprendizaje técnico radicó en la adopción del patrón Smart and Dumb Components: al delegar la "
        "responsabilidad visual a ProductoListComponent y concentrar el gobierno de estados en ProductosPageComponent, "
        "la aplicación gana testeabilidad, reusabilidad y una mantenibilidad superior frente a diseños acoplados."
    )
    apply_font(r_rf1, name="Arial", size_pt=10.5, color_rgb=COLOR_TEXT_BODY)
    
    p_refl2 = add_paragraph_styled(doc, space_after=6)
    r_rf2 = p_refl2.add_run(
        "Asimismo, la formalización explícita del ciclo de estados asíncronos (EstadoUi: 'idle' | 'loading' | "
        "'success' | 'empty' | 'error') demostró ser un pilar crítico en la experiencia de usuario y la resiliencia "
        "del software. Evitar estados indeterminados mediante renderizado condicional exhaustivo y proporcionar "
        "mecanismos de recuperación defensiva (botón de reintento) garantiza que el cliente no colapse ante "
        "intermitencias de red. Finalmente, la verificación unitaria con HttpTestingController permitió aislar las "
        "pruebas del servidor real, confirmando que la gobernanza basada en ramas y revisiones cruzadas estrictas "
        "mantiene una cadencia de entrega profesional, determinista y trazable."
    )
    apply_font(r_rf2, name="Arial", size_pt=10.5, color_rgb=COLOR_TEXT_BODY)

    return doc

if __name__ == "__main__":
    doc = create_document()
    
    os.makedirs("docs", exist_ok=True)
    out_docs = "docs/P10_Seccion_IV_Victor_Reyes.docx"
    doc.save(out_docs)
    print(f"Documento generado exitosamente en: {out_docs}")
    
    desktop_dir = os.path.expanduser("~/Desktop")
    if os.path.exists(desktop_dir):
        out_desktop = os.path.join(desktop_dir, "P10_Seccion_IV_Victor_Reyes.docx")
        shutil.copy2(out_docs, out_desktop)
        print(f"Copia creada en el Escritorio: {out_desktop}")
