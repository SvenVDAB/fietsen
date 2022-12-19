insert into campussen(naam, straat, huisNr, postcode, gemeente)
VALUES('test', 'test', 'test', 'test', 'test');
insert into campussentelefoonnrs(campusId, nummer, opmerking, fax) VALUES
((select id from campussen where naam = 'test'), '0486960547', 'test', false);