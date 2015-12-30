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

#
# In the 'my.ini' file, update the following property:
# max_allowed_packet=64M
#

#
# As the 'root' MySQL user, run the following commands:
#
create database otapubs;
create user 'otadmin'@'localhost' identified by 'otadmin';
grant all privileges on otapubs . * to 'otadmin'@'localhost';

