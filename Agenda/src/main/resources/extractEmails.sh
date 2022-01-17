#
# Copyright (c) 2015-2022, Patricia Maraver
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#  
#

#mongoexport -d emails -c contact --type=csv --query '{"unsubscribed":false,"emailList.bounced":false}' --out /var/www/litermate/assets/contacts.csv --fields firstName,lastName,emailList.email
mongo emails --eval 'db.contact.aggregate( [
   {$unwind: "$emailList"},
   {$match: {"unsubscribed":false, "emailList.bounced":false}},
   {$project: { _id:0,firstName:"$firstName",lastName:"$lastName", "email": "$emailList.email"}},
   {$out: "contact_csv"}
]);'
mongoexport -d emails -c contact_csv --type=csv --query '{}' --out /var/www/litermate/assets/contacts.csv --fields firstName,lastName,email
