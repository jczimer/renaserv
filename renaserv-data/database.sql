#

create table evento (
	id int not null primary key,
	descricao varchar(255)
);

create table proprietario (
	credential int not null,
	id int not null,
	nome varchar(255),
	telefones varchar(1000),
	contatos varchar(1000),
	primary key (credential,id),
	constraint fk_proprietario_credential foreign key (credential) references credential(id)
);


create table veiculo (
	credential int not null,
	id int not null,
	placa varchar(50),
	modelo varchar(255),
	chassi varchar(255),
	renavam varchar(255),
	cor varchar(100),
	ano varchar(50),		
	lat float,
	lon float,
	speed float,
	io int,	
	direction int,
	primary key (credential,id),
	constraint fk_veiculo_credential foreign key (credential) references credential(id)
);
alter table veiculo add constraint fk_veiculo_propritario foreign key (credential,proprietario) references proprietario (credential,id)

create table ocorrencia (
	credential int not null,
	veiculo int not null,
	evento int not null,
	data_dados datetime not null,
	data_cad datetime not null,
	lat float,
	lon float,
	speed float,
	io int,	
	direction int,
	event_id bigint,
	data_tratativa datetime,
	usuario_tratativa varchar(255),
	status int,
	constraint pk_ocorrencia primary key (credential,evento,veiculo,data_dados)
	
);
alter table ocorrencia add constraint fk_ocorrencia_veiculo foreign key (veiculo) references veiculo(id);
alter table ocorrencia add constraint fk_ocorrencia_evento foreign key (evento) references evento(id);

create table evento_monitorado (
	
	credential int not null,
	evento int not null,
	primary key (credential, evento),
	
);

create table usuario (
	id int not null primary key auto_increment,
	usuario varchar(100),
	senha varchar(100),
	token varchar(100),
	ip varchar(50)	

)