import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Producto, CrearProductoRequest } from '../models/producto.model';

@Injectable({
  providedIn: 'root'
})
export class ProductosService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/productos';

  obtenerTodos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl).pipe(
      catchError(this.manejadorError)
    );
  }

  obtenerPorId(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.manejadorError)
    );
  }

  crearProducto(producto: CrearProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto).pipe(
      catchError(this.manejadorError)
    );
  }

  private manejadorError(error: HttpErrorResponse): Observable<never> {
    let mensaje = 'Ocurrió un error inesperado al comunicarse con el servidor.';
    const esErrorProxy =
      error.status === 500 &&
      typeof error.error === 'string' &&
      /error occurr?ed while trying to proxy|proxy error|econnrefused|econnreset|etimedout|enotfound/i.test(error.error);

    if (error.status === 0 || error.status === 502 || error.status === 504 || esErrorProxy) {
      mensaje = 'No fue posible conectar con la API REST. Verifique su conexión o si el servidor backend está activo.';
    } else if (error.error && error.error.detail) {
      mensaje = error.error.detail;
    } else if (error.status === 404) {
      mensaje = 'El recurso solicitado no fue encontrado.';
    }
    return throwError(() => new Error(mensaje));
  }
}
