export type EstadoUiTipo = 'idle' | 'loading' | 'success' | 'empty' | 'error';

export interface EstadoUi {
  tipo: EstadoUiTipo;
  mensajeError?: string;
}
