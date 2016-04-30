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

CREATE TABLE code_list (
	id BIGINT NOT NULL AUTO_INCREMENT,
	release_date DATE NOT NULL,
	archive_filename VARCHAR(50) NOT NULL,
	archive_content_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (archive_content_id) REFERENCES file_content(id)
);

CREATE TABLE code_list_download (
	code_list_id BIGINT NOT NULL,
	registrant_id BIGINT NOT NULL,
	PRIMARY KEY (code_list_id, registrant_id),
	FOREIGN KEY (code_list_id) REFERENCES code_list(id),
	FOREIGN KEY (registrant_id) REFERENCES registrant(id)
);
