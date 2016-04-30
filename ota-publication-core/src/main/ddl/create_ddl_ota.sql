#
# Copyright (C) 2015 OpenTravel Alliance (info@opentravel.org)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

CREATE TABLE registrant (
	id BIGINT NOT NULL AUTO_INCREMENT,
	last_name VARCHAR(50) NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	company VARCHAR(100),
	title VARCHAR(100),
	email VARCHAR(50) NOT NULL,
	phone VARCHAR(50),
	ota_member BOOLEAN NOT NULL DEFAULT 0,
	registration_date DATE,
	PRIMARY KEY (id)
);

CREATE TABLE file_content (
	id BIGINT NOT NULL AUTO_INCREMENT,
	file_bytes LONGBLOB NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE publication (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	pub_type VARCHAR(20) NOT NULL,
	pub_state VARCHAR(20) NOT NULL,
	pub_date TIMESTAMP NOT NULL,
	archive_filename VARCHAR(50) NOT NULL,
	archive_content_id BIGINT NOT NULL,
	release_notes_filename VARCHAR(50) NOT NULL,
	release_notes_content_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (archive_content_id) REFERENCES file_content(id),
	FOREIGN KEY (release_notes_content_id) REFERENCES file_content(id)
);

CREATE TABLE publication_group (
	id BIGINT NOT NULL AUTO_INCREMENT,
	publication_id BIGINT NOT NULL,
	name VARCHAR(100) NOT NULL,
	member_type VARCHAR(20) NOT NULL,
	sort_order INT NOT NULL,
	removed BOOLEAN NOT NULL DEFAULT 0,
	PRIMARY KEY (id),
	FOREIGN KEY (publication_id) REFERENCES publication(id)
);

CREATE TABLE publication_item (
	id BIGINT NOT NULL AUTO_INCREMENT,
	group_id BIGINT NOT NULL,
	item_type VARCHAR(20) NOT NULL,
	item_filename VARCHAR(200) NOT NULL,
	item_content_id BIGINT NOT NULL,
	file_size BIGINT NOT NULL,
	create_date TIMESTAMP NOT NULL,
	removed BOOLEAN NOT NULL DEFAULT 0,
	PRIMARY KEY (id),
	FOREIGN KEY (group_id) REFERENCES publication_group(id),
	FOREIGN KEY (item_content_id) REFERENCES file_content(id)
);

CREATE TABLE comment (
	id BIGINT NOT NULL AUTO_INCREMENT,
	comment_number INT NOT NULL,
	publication_item_id BIGINT,
	other_item_name VARCHAR(200),
	pub_state VARCHAR(20) NOT NULL,
	registrant_id BIGINT NOT NULL,
	comment_type VARCHAR(20) NOT NULL,
	comment_text TEXT NOT NULL,
	suggested_change TEXT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (publication_item_id) REFERENCES publication_item(id),
	FOREIGN KEY (registrant_id) REFERENCES registrant(id)
);

CREATE TABLE schema_comment (
	id BIGINT NOT NULL AUTO_INCREMENT,
	comment_xpath VARCHAR(200) NOT NULL,
	modify_xpath VARCHAR(200) NOT NULL,
	new_annotations TEXT NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE artifact_comment (
	id BIGINT NOT NULL AUTO_INCREMENT,
	page_numbers VARCHAR(100) NOT NULL,
	line_numbers VARCHAR(100) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE comment_counter (
	next_val INT NOT NULL
);

CREATE TABLE publication_download (
	publication_id BIGINT NOT NULL,
	registrant_id BIGINT NOT NULL,
	PRIMARY KEY (publication_id, registrant_id),
	FOREIGN KEY (publication_id) REFERENCES publication(id),
	FOREIGN KEY (registrant_id) REFERENCES registrant(id)
);

CREATE TABLE publication_item_download (
	publication_item_id BIGINT NOT NULL,
	registrant_id BIGINT NOT NULL,
	PRIMARY KEY (publication_item_id, registrant_id),
	FOREIGN KEY (publication_item_id) REFERENCES publication_item(id),
	FOREIGN KEY (registrant_id) REFERENCES registrant(id)
);

CREATE TABLE hibernate_sequence (
	next_val BIGINT(20)
);
INSERT INTO hibernate_sequence VALUES ( 1000 );

CREATE TABLE users (
	username VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles (
	username VARCHAR(50) NOT NULL,
	rolename VARCHAR(50) NOT NULL
);

# Default userid/password = admin/admin
INSERT INTO users VALUES ("admin", "d033e22ae348aeb5660fc2140aec35850c4da997");
INSERT INTO user_roles VALUES ("admin", "administrators");

SOURCE create_ddl_ota_update1.sql
SOURCE create_ddl_ota_update2.sql
