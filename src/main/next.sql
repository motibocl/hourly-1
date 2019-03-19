#18/5/18
INSERT INTO election.translations(translation_key, translation_value, en) VALUES ('unverified.supporting', 'תומך פוטנציאלי', '');
UPDATE translations SET translation_value='שיחות ומוקדנים' WHERE translation_key='calls.report';
UPDATE translations SET translation_value='מוקדן' WHERE translation_key='caller';
INSERT INTO election.config(config_key, config_value) VALUES ('gmail_user_name', 'elector.inner@gmail.com');
INSERT INTO election.config(config_key, config_value) VALUES ('gmail_password', 'sigi4646');
INSERT INTO election.config(config_key, config_value) VALUES ('prod', 'true');
UPDATE config SET config_value='true' WHERE config_key='send_emails';






#31/5/18
ALTER TABLE voters MODIFY COLUMN support_sign INT DEFAULT '0';
ALTER TABLE voters MODIFY COLUMN support_status INT DEFAULT '0';
#2/6/18
INSERT INTO translations(translation_key, translation_value, en) VALUES('wrong.format.excel', 'פורמט שגוי באקסל', 'Wrong Excel Format');
INSERT INTO translations(translation_key, translation_value, en) VALUES('the.excel.file.is.wrongly.formatted.check.line', 'בקובץ האקסל שהעלית ישנם שגיאות. בדוק את השורה הבאה: ', 'Errors found in your uploaded excel file. Check the following line: ');


#14/6/18
ALTER TABLE ballot_boxes DROP street;
ALTER TABLE ballot_boxes DROP type;
ALTER TABLE ballot_boxes DROP fromHouse;
ALTER TABLE ballot_boxes DROP to_house;
ALTER TABLE ballot_boxes DROP from_letter;
ALTER TABLE ballot_boxes DROP to_letter;

#19-6-18
DELETE FROM election.contacts_groups;