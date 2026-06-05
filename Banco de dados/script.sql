CREATE DATABASE loja_toptop
character set utf8mb4
collate utf8mb4_general_ci;

use loja_toptop;

create table clientes(
	id_cliente int primary key auto_increment,
    nome varchar(150) not null,
    email varchar(150) unique,
    telefone varchar(11) not null,
    logradouro varchar(60) not null,
    bairro varchar(50) not null
);

create table produtos(
	id_produto int primary key auto_increment,
    nome_produto varchar(100) not null,
    estoque int not null,
    preco double,
	categoria varchar(50)
);

create table pedido(
	id_pedido int primary key auto_increment,
    cliente_id int,
    status varchar(50) not null,
    foreign key(cliente_id) references clientes(id_cliente)
);

create table itens_pedidos(
	produto_id int,
    pedido_id int,
	quantidade_itens int not null,
    foreign key(pedido_id) references pedido(id_pedido),
    foreign key(produto_id) references produtos(id_produto)
);

