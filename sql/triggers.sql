create or replace function prow_zaj_dodaj () RETURNS TRIGGER AS $$
DECLARE
    b BOOLEAN;
BEGIN
  SELECT EXISTS (select * from prow_zaj pw where pw.id_prowadzacy=New.id_prowadzacy and pw.id_zajecia=New.id_zajecia) INTO b;
  IF (SELECT b = FALSE) THEN
    INSERT INTO prow_zaj (id_prowadzacy, id_zajecia) VALUES (New.id_prowadzacy, New.id_zajecia) ;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER zajecia_dodaj_t
    AFTER INSERT ON pojedyncze_zajecia
    FOR EACH ROW EXECUTE PROCEDURE prow_zaj_dodaj();



