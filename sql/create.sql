create table klient
(
	id_klient int generated always as identity,
	imie varchar(30) not null,
	nazwisko varchar(30) not null,
	tel varchar(9) not null,
	email varchar(30) not null,
    login varchar(30) not null,
    haslo varchar(30) not null,
	primary key(id_klient)
)

create table prowadzacy
(
	id_prowadzacy int generated always as identity,
	imie varchar(30) not null,
	nazwisko varchar(30) not null,
	tel varchar(9) not null,
	email varchar(30) not null,
    login varchar(30) not null,
    haslo varchar(30) not null,
	primary key(id_prowadzacy)
)

create table kategoria
(
	id_kategoria int generated always as identity,
	nazwa varchar(30) not null,
	primary key(id_kategoria)
)

create table zajecia_opis
(
	id_opis int generated always as identity,
	opis varchar(300) not null,
	primary key(id_opis)
)

create table zajecia
(
	id_zajecia int generated always as identity,
	id_opis int not null,
	id_kategoria int not null,
    nazwa varchar(30) not null,
	primary key(id_zajecia),
	constraint fk_nazwa
		foreign key (id_opis)
			references zajecia_opis(id_opis),
	constraint fk_kategoria
		foreign key (id_kategoria)
			references kategoria(id_kategoria)
)

create table prow_zaj
(
	id_prow_zaj int generated always as identity,
	id_prowadzacy int not null,
	id_zajecia int not null,
	primary key(id_prow_zaj),
	constraint fk_prowadzacy
		foreign key (id_prowadzacy)
			references prowadzacy(id_prowadzacy),
	constraint fk_zajecia
		foreign key (id_zajecia)
			references zajecia(id_zajecia)
)

create table sala
(
	id_sala int generated always as identity,
    nazwa varchar(30) not null,
	ilosc_miejsc int not null,
	primary key(id_sala)
)

create table sprzet
(
	id_sprzet int generated always as identity,
	id_nazwa varchar(30) not null,
	primary key(id_sprzet)
)

create table sprzet_sala
(
	id_sprzet_sala int generated always as identity,
	id_sprzet int not null,
	id_sala int not null,
    ilosc int not null,
	primary key(id_sprzet_sala),
	constraint fk_sprzet
		foreign key (id_sprzet)
			references sprzet(id_sprzet),
	constraint fk_sala
		foreign key (id_sala)
			references sala(id_sala)
)

create table zajecia_personalne
(
	id_zajpers int generated always as identity,
	id_klient int not null,
	id_sala int not null,
	id_prowadzacy int not null,
	termin varchar(30) not null,
	cena decimal not null,
	primary key(id_zajpers),
	constraint fk_klient
		foreign key (id_klient)
			references klient(id_klient),
	constraint fk_sala
		foreign key (id_sala)
			references sala(id_sala),
	constraint fk_prowadzacy
		foreign key (id_prowadzacy)
			references prowadzacy(id_prowadzacy)
)

create table pojedyncze_zajecia
(
	id_pzaj int generated always as identity,
	id_zajecia int not null,
	id_sala int not null,
	id_prowadzacy int not null,
	termin varchar(30) not null,
	cena decimal not null,
	primary key(id_pzaj),
	constraint fk_zajecia
		foreign key (id_zajecia)
			references zajecia(id_zajecia),
	constraint fk_sala
		foreign key (id_sala)
			references sala(id_sala),
	constraint fk_prowadzacy
		foreign key (id_prowadzacy)
			references prowadzacy(id_prowadzacy)
)

create table rezerwacja
(
	id_rezerwacja int generated always as identity,
	id_klient int not null,
	id_pzaj int not null,
	status varchar(30) not null,
	primary key(id_rezerwacja),
	constraint fk_klient
		foreign key (id_klient)
			references klient(id_klient),
	constraint fk_pzaj
		foreign key (id_pzaj)
			references pojedyncze_zajecia(id_pzaj)
)
