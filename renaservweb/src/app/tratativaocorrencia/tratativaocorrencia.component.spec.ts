import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TratativaocorrenciaComponent } from './tratativaocorrencia.component';

describe('TratativaocorrenciaComponent', () => {
  let component: TratativaocorrenciaComponent;
  let fixture: ComponentFixture<TratativaocorrenciaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TratativaocorrenciaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TratativaocorrenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
