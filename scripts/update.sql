UPDATE core.package p
JOIN core.license l ON p.licenseName = l.name
SET licenseId = l.id
;
UPDATE core.package p
JOIN core.arch a ON p.archName = a.name
SET archId = a.id
;
UPDATE core.package p
JOIN core.packager r ON p.packagerName = r.name
SET packagerId = r.id
;