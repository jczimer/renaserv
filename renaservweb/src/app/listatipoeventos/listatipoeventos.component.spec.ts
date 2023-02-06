import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListatipoeventosComponent } from './listatipoeventos.component';

describe('ListatipoeventosComponent', () => {
  let component: ListatipoeventosComponent;
  let fixture: ComponentFixture<ListatipoeventosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListatipoeventosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListatipoeventosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
