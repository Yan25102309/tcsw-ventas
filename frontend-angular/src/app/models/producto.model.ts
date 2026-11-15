export interface Producto {
  id: number;
  nombre: string;
  precio: number;
  existencia: number;
}

export interface CrearProductoRequest {
  id: number;
  nombre: string;
  precio: number;
  existencia: number;
}
