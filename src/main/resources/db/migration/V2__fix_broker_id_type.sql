ALTER TABLE broker_config ALTER COLUMN id TYPE UUID USING id::uuid;
