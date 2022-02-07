create view rezerwacja_info
AS
select k.nazwa as kategoria, z.nazwa, s.nazwa as sala, p.imie, p.nazwisko, pz.termin, pz.cena, r.status, id_klient from rezerwacja r 
join pojedyncze_zajecia pz using(id_pzaj)
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
join zajecia z using(id_zajecia)
join kategoria k using(id_kategoria);

create view zajpersonalne_info
AS
select p.id_prowadzacy, p.imie as pimie, p.nazwisko as pnazwisko, k.id_klient, k.imie as kimie, k.nazwisko as knazwisko, s.nazwa as sala, zp.termin, zp.cena from zajecia_personalne zp 
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
join klient k using(id_klient);

create view pojzaj_info
AS
select pz.id_prowadzacy, p.imie, p.nazwisko, k.nazwa as kategoria, z.nazwa, s.nazwa as sala, pz.termin from pojedyncze_zajecia pz
join sala s using(id_sala)
join zajecia z using (id_zajecia)
join prowadzacy p using(id_prowadzacy)
join kategoria k using (id_kategoria);

create view zajpers_klient_report
AS
select k.id_klient, k.imie, k.nazwisko, COUNT(*) as ilosc_zajec, SUM(zp.cena) as cena from zajecia_personalne zp 
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
join klient k using(id_klient) 
group by k.imie, k.nazwisko, k.id_klient;

create view rezniep_klient_report
as
select kl.id_klient, kl.imie, kl.nazwisko, COUNT(*) as ilosc_rez, SUM(pz.cena) from rezerwacja r 
join pojedyncze_zajecia pz using(id_pzaj)
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
join zajecia z using(id_zajecia)
join kategoria k using(id_kategoria)
join klient kl using(id_klient)
group by kl.id_klient, kl.imie, kl.nazwisko, r.status
having r.status='niepotwierdzono';

create view rezpot_klient_report
as
select kl.id_klient, kl.imie, kl.nazwisko, COUNT(*) as ilosc_rez, SUM(pz.cena) from rezerwacja r 
join pojedyncze_zajecia pz using(id_pzaj)
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
join zajecia z using(id_zajecia)
join kategoria k using(id_kategoria)
join klient kl using(id_klient)
group by kl.id_klient, kl.imie, kl.nazwisko, r.status
having r.status='potwierdzono';

create view zajecia_prowadzacy_report
as
select p.id_prowadzacy, p.imie, p.nazwisko, COUNT(*) as ilosc_zajec, SUM(zp.cena) as cena from pojedyncze_zajecia zp 
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
group by p.id_prowadzacy, p.imie, p.nazwisko;

create view zajpers_prowadzacy_report
as
select p.id_prowadzacy, p.imie, p.nazwisko, COUNT(*) as ilosc_zajec, SUM(zp.cena) as cena from zajecia_personalne zp 
join sala s using(id_sala)
join prowadzacy p using(id_prowadzacy)
group by p.id_prowadzacy, p.imie, p.nazwisko;
