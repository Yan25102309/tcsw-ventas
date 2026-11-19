import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductosService } from './productos.service';
import { Producto, CrearProductoRequest } from '../models/producto.model';

describe('ProductosService', () => {
  let service: ProductosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductosService]
    });
    service = TestBed.inject(ProductosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe crearse correctamente el servicio', () => {
    expect(service).toBeTruthy();
  });

  it('debe retornar lista de productos en obtenerTodos()', () => {
    const dummyProductos: Producto[] = [
      { id: 1, nombre: 'Teclado RGB', precio: 800.0, existencia: 10 },
      { id: 2, nombre: 'Mouse Gamer', precio: 400.0, existencia: 5 }
    ];

    service.obtenerTodos().subscribe(productos => {
      expect(productos.length).toBe(2);
      expect(productos).toEqual(dummyProductos);
    });

    const req = httpMock.expectOne('/api/v1/productos');
    expect(req.request.method).toBe('GET');
    req.flush(dummyProductos);
  });

  it('debe retornar un producto específico en obtenerPorId()', () => {
    const dummyProducto: Producto = { id: 1, nombre: 'Teclado RGB', precio: 800.0, existencia: 10 };

    service.obtenerPorId(1).subscribe(producto => {
      expect(producto).toEqual(dummyProducto);
    });

    const req = httpMock.expectOne('/api/v1/productos/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummyProducto);
  });

  it('debe enviar POST y retornar producto creado en crearProducto()', () => {
    const nuevoProducto: CrearProductoRequest = { id: 3, nombre: 'Monitor 4K', precio: 6500.0, existencia: 4 };
    const dummyCreado: Producto = { ...nuevoProducto };

    service.crearProducto(nuevoProducto).subscribe(producto => {
      expect(producto).toEqual(dummyCreado);
    });

    const req = httpMock.expectOne('/api/v1/productos');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(nuevoProducto);
    req.flush(dummyCreado);
  });

  it('debe manejar errores de red HTTP adecuadamente (status 0)', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toContain('No fue posible conectar');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.error(new ProgressEvent('error'), { status: 0, statusText: 'Unknown Error' });
  });

  it('debe manejar error 404 adecuadamente', () => {
    service.obtenerPorId(999).subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('El recurso solicitado no fue encontrado.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos/999');
    req.flush('Not Found', { status: 404, statusText: 'Not Found' });
  });

  it('debe extraer el mensaje de detalle cuando el error contiene detail (Problem Details RFC 9457)', () => {
    const errorBody = { detail: 'El producto con ID especificado ya existe.' };

    service.crearProducto({ id: 1, nombre: 'Repetido', precio: 100, existencia: 1 }).subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('El producto con ID especificado ya existe.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush(errorBody, { status: 409, statusText: 'Conflict' });
  });

  it('debe devolver mensaje genérico cuando status es 500 sin detalle', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('Ocurrió un error inesperado al comunicarse con el servidor.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush('Error interno', { status: 500, statusText: 'Internal Server Error' });
  });

  it('debe devolver mensaje de API desconectada cuando el proxy responde 504 Gateway Timeout', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('No fue posible conectar con la API REST. Verifique su conexión o si el servidor backend está activo.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush('Error occurred while trying to proxy: localhost:4200/api/v1/productos', {
      status: 504,
      statusText: 'Gateway Timeout'
    });
  });

  it('debe devolver mensaje de API desconectada cuando el proxy responde 502 Bad Gateway', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('No fue posible conectar con la API REST. Verifique su conexión o si el servidor backend está activo.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush('Bad Gateway', { status: 502, statusText: 'Bad Gateway' });
  });

  it('debe devolver mensaje de API desconectada cuando el proxy responde 500 en texto plano de proxy', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('No fue posible conectar con la API REST. Verifique su conexión o si el servidor backend está activo.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush('Error occurred while trying to proxy: localhost:4200/api/v1/productos', {
      status: 500,
      statusText: 'Internal Server Error'
    });
  });

  it('debe devolver mensaje de API desconectada cuando el proxy responde 500 con ECONNREFUSED o Proxy error', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('No fue posible conectar con la API REST. Verifique su conexión o si el servidor backend está activo.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush('Proxy error: Could not proxy request /api/v1/productos (ECONNREFUSED)', {
      status: 500,
      statusText: 'Internal Server Error'
    });
  });

  it('debe preservar el detalle RFC 9457 cuando la API real responde 500 con detail', () => {
    const errorBody = {
      type: 'https://tcsw-ventas.invalid/errors/persistence-failure',
      title: 'Fallo de Persistencia',
      status: 500,
      detail: 'Error en la capa de datos. Verifique la consistencia del registro.',
      instance: '/api/v1/productos'
    };

    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('Error en la capa de datos. Verifique la consistencia del registro.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush(errorBody, { status: 500, statusText: 'Internal Server Error' });
  });

  it('debe devolver mensaje genérico cuando la API responde 500 JSON sin campo detail', () => {
    service.obtenerTodos().subscribe({
      next: () => fail('Debería haber fallado'),
      error: (err: Error) => {
        expect(err.message).toBe('Ocurrió un error inesperado al comunicarse con el servidor.');
      }
    });

    const req = httpMock.expectOne('/api/v1/productos');
    req.flush({ title: 'Internal Server Error' }, { status: 500, statusText: 'Internal Server Error' });
  });
});
