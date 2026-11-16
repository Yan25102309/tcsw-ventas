/// <reference types="jasmine" />
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ProductoFormComponent } from './producto-form.component';

describe('ProductoFormComponent', () => {
  let component: ProductoFormComponent;
  let fixture: ComponentFixture<ProductoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        ProductoFormComponent,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe crear el componente de formulario', () => {
    expect(component).toBeTruthy();
  });

  it('debe marcar el formulario como inválido cuando está vacío', () => {
    expect(component.formulario.valid).toBeFalse();
  });

  it('debe validar que el precio no sea negativo', () => {
    const controlPrecio = component.formulario.controls['precio'];
    controlPrecio.setValue(-10);
    expect(controlPrecio.valid).toBeFalse();
  });
});
