ALTER TABLE medicines ADD COLUMN price_id bigint;
ALTER TABLE medicines ADD COLUMN pharmacy_id bigint;
ALTER TABLE medicines ADD CONSTRAINT fk_medicine FOREIGN KEY(price_id,pharmacy_id)
            REFERENCES prices(medicine_id,pharmacy_id);
ALTER TABLE pharmacies ADD COLUMN medicine_id bigint;
ALTER TABLE pharmacies ADD CONSTRAINT  fk_medicine FOREIGN KEY(medicine_id) REFERENCES medicines(id);
ALTER TABLE prices ADD CONSTRAINT  fk_pharmacy FOREIGN KEY(pharmacy_id) REFERENCES pharmacies(id);
ALTER TABLE prices ADD CONSTRAINT  fk_medicine FOREIGN KEY(medicine_id) REFERENCES medicines(id);
