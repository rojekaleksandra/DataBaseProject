create or replace function klient_zaloguj(klient_login character varying, klient_haslo character varying) returns boolean
    language plpgsql
as
$$
DECLARE
        dane BOOLEAN;
    BEGIN
            SELECT EXISTS (select k.login, k.haslo from klient k where k.login = klient_login and k.haslo = klient_haslo) into dane;
        if (SELECT dane = TRUE) THEN
            return true;
        else
           RAISE EXCEPTION 'Zły login lub hasło!';
        end if;
    END;
$$;


create or replace function prowadzacy_zaloguj(prowadzacy_login character varying, prowadzacy_haslo character varying) returns boolean
    language plpgsql
as
$$
DECLARE
        dane BOOLEAN;
    BEGIN
            SELECT EXISTS (select p.login, p.haslo from prowadzacy p where p.login = prowadzacy_login and p.haslo = prowadzacy_haslo) into dane;
        if (SELECT dane = TRUE) THEN
            return true;
        else
           RAISE EXCEPTION 'Zły login lub hasło!';
        end if;
    END;
$$;

create or replace function klient_zarejestruj(klient_imie character varying, klient_nazwisko character varying, klient_tel character varying, klient_email character varying, klient_login character varying, klient_haslo character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from klient k where k.email=klient_email) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into klient (imie, nazwisko, tel, email, login, haslo) values (klient_imie, klient_nazwisko, klient_tel, klient_email, klient_login, klient_haslo);
    ELSE
        RAISE EXCEPTION 'Ten email jest już w bazie danych!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function prowadzacy_zarejestruj (prowadzacy_imie character varying, prowadzacy_nazwisko character varying, prowadzacy_tel character varying, prowadzacy_email character varying, prowadzacy_login character varying, prowadzacy_haslo character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from prowadzacy p where p.email=prowadzacy_email) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into prowadzacy (imie, nazwisko, tel, email, login, haslo) values (prowadzacy_imie, prowadzacy_nazwisko, prowadzacy_tel, prowadzacy_email, prowadzacy_login, prowadzacy_haslo);
    ELSE
        RAISE EXCEPTION 'Ten email jest już w bazie danych!';
    END IF;
    RETURN TRUE;
END;
$$;


create or replace function rezerwacja_dodaj(rez_id_klient integer, rez_id_pzaj integer, rez_status character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
    max integer;
    cnt integer;
BEGIN
    SELECT EXISTS (select r.id_klient, r.id_pzaj from rezerwacja r where r.id_klient = rez_id_klient and r.id_pzaj=rez_id_pzaj) INTO b;
    select count (r.id_pzaj) from rezerwacja r where r.id_pzaj=rez_id_pzaj into cnt;
    select s.ilosc_miejsc from sala s join pojedyncze_zajecia pz using(id_sala) join rezerwacja r using(id_pzaj) where r.id_pzaj=4 limit 1 into max;
    IF (SELECT b = FALSE AND cnt<max) THEN
        insert into rezerwacja (id_klient, id_pzaj, status) values (rez_id_klient, rez_id_pzaj, rez_status);
    ELSE
        RAISE EXCEPTION 'Rezerwacja nie powiodła się! Taka rezerwacja już istnieje, bądź brak miejsc!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function zajecia_personalne_dodaj(zp_id_klient integer, zp_id_sala integer, zp_id_prowadzacy integer, zp_termin character varying, zp_cena decimal) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
    b1 BOOLEAN;
    b2 BOOLEAN;
BEGIN
    SELECT EXISTS (select zp.id_klient, zp.id_prowadzacy, zp.termin from zajecia_personalne zp where zp.id_klient = zp_id_klient and zp.id_prowadzacy=zp_id_prowadzacy and zp.termin=zp.termin) INTO b;
    SELECT EXISTS (select zp.id_sala from zajecia_personalne zp where zp.id_sala=zp_id_sala and zp.termin=zp_termin) INTO b1;
    SELECT EXISTS (select pz.id_sala from pojedyncze_zajecia pz where pz.id_sala=zp_id_sala and pz.termin=zp_termin) INTO b2;
    IF (SELECT b = FALSE AND b1=FALSE and b2=FALSE) THEN
        insert into zajecia_personalne (id_klient, id_sala, id_prowadzacy, termin, cena) values (zp_id_klient, zp_id_sala, zp_prowadzacy, zp_termin, zp_cena);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Takie zajęcia personalne już istnieją, bądź sala jest zajęta!';
    END IF;
    RETURN TRUE;
END;
$$;


create or replace function zajeciapoj_dodaj(pz_id_zajecia integer, pz_id_sala integer, pz_id_prowadzacy integer, pz_termin character varying, pz_cena decimal) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
    b1 BOOLEAN;
    b2 BOOLEAN;
BEGIN
    SELECT EXISTS (select pz.id_zajecia, pz.id_prowadzacy, pz.termin from pojedyncze_zajecia pz where pz.id_zajecia=pz_id_zajecia and pz.id_prowadzacy=pz_id_prowadzacy and pz.termin=pz.termin) INTO b;
    SELECT EXISTS (select zp.id_sala from zajecia_personalne zp where zp.id_sala=pz_id_sala and zp.termin=pz_termin) INTO b1;
    SELECT EXISTS (select pz.id_sala from pojedyncze_zajecia pz where pz.id_sala=pz_id_sala and pz.termin=pz_termin) INTO b2;
    IF (SELECT b = FALSE AND b1=FALSE and b2=FALSE) THEN
        insert into pojedyncze_zajecia (id_zajecia, id_sala, id_prowadzacy, termin, cena) values (pz_id_zajecia, pz_id_sala, pz_id_prowadzacy, pz_termin, pz_cena);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Takie zajęcia już istnieją, bądź sala jest zajęta!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function kategoria_dodaj(k_nazwa character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from kategoria k where k.nazwa=k_nazwa) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into kategoria (nazwa) values (k_nazwa);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Taka kategoria juz istnieje!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function opis_dodaj(o_opis character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from zajecia_opis zo where zo.opis=o_opis) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into zajecia_opis (opis) values (o_opis);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Taki opis juz istnieje!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function zajecia_dodaj(z_id_opis integer, z_id_kategoria integer, z_nazwa character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from zajecia z where z.nazwa=z_nazwa) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into zajecia (id_opis, id_kategoria, nazwa) values (z_id_opis, z_id_kategoria, z_nazwa);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Takie zajęcia juz istnieją!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function sala_dodaj(s_nazwa character varying, s_ilosc_miejsc integer) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from sala s where s.nazwa=s_nazwa) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into sala (nazwa, ilosc_miejsc) values (s_nazwa, s_ilosc_miejsc);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Taka sala juz istnieje!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function sprzet_dodaj(s_nazwa character varying) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from sprzet s where s.id_nazwa=s_nazwa) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into sprzet (id_nazwa) values (s_nazwa);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Taki sprzet juz istnieje!';
    END IF;
    RETURN TRUE;
END;
$$;

create or replace function sprzetsala_dodaj(s_sprzet integer, s_sala integer, s_ilosc integer) returns boolean
    language plpgsql
as
$$
DECLARE
    b BOOLEAN;
BEGIN
    SELECT EXISTS (select * from sprzet_sala s where s.id_sprzet=s_sprzet and s.id_sala=s_sala) INTO b;
    IF (SELECT b = FALSE) THEN
        insert into sprzet_sala (id_sprzet, id_sala, ilosc) values (s_sprzet, s_sala, s_ilosc);
    ELSE
        RAISE EXCEPTION 'Operacja nie powiodła się! Ten sprzet jest juz dodany do sali!';
    END IF;
    RETURN TRUE;
END;
$$;

