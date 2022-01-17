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

mongo literature-review --eval 'db.article.positives.update( 
    {$or: [{"reconstructions.currentStatusList.specificDetails": "NO_CONTACT_INFORMATION"},
                {"reconstructions.currentStatusList.specificDetails": "NO_RESPONSE"},
                {"reconstructions.currentStatusList.specificDetails": "REQUEST_DECLINED"},
                {"reconstructions.currentStatusList.specificDetails": "LOST_DATA"},
                {"reconstructions.currentStatusList.specificDetails": "COMMUNICATION_STOPPED"},
                {"reconstructions.currentStatusList.specificDetails": "BOUNCED"},
                {"reconstructions.currentStatusList.specificDetails": "BOUNCED_NEGATIVE"}

            ]},
                { $set: { "reconstructions.globalStatus": "Not available"}},
    false,
    true
);
db.article.positives.update(
    {$or: [{"reconstructions.currentStatusList.specificDetails": "TO_BE_REQUESTED"},
            {"reconstructions.currentStatusList.specificDetails": "POSITIVE_RESPONSE"},
            {"reconstructions.currentStatusList.specificDetails": "INVITED"},
            {"reconstructions.currentStatusList.specificDetails": "REMINDER"},
            {"reconstructions.currentStatusList.specificDetails": "PESTERING"},
            {"reconstructions.currentStatusList.specificDetails": "ULTIMATUM"}

        ]},
    { $set: { "reconstructions.globalStatus": "Determining availability"}},
    false,
    true
);
db.article.positives.update(
    {$or: [{"reconstructions.currentStatusList.specificDetails": "IN_PROCESSING_PIPELINE"},
            {"reconstructions.currentStatusList.specificDetails": "IN_REPOSITORY"},
            {"reconstructions.currentStatusList.specificDetails": "IN_RELEASE"},
            {"reconstructions.currentStatusList.specificDetails": "ON_HOLD"}
        ]},
    { $set: { "reconstructions.globalStatus": "Available"}},
    false,
    true
);
db.article.positives.find({"sharedList": {$exists: true}, "reconstructions.globalStatus": {$exists: false}, "data.dataUsage": "SHARING" }).forEach(function (a) {
    for (i = 0; i<a.sharedList.length; i++){
        db.article.positives.find({"_id":  a.sharedList[i].sharedId}).forEach(function (sharedArticle) {
            a.reconstructions = {};
            if (sharedArticle.reconstructions != null){
                a.reconstructions.globalStatus = sharedArticle.reconstructions.globalStatus;
                db.article.positives.save(a);
            } else{
                print('Article: ' + a._id + ' shared without reconstructions: ' + sharedArticle._id);
            }
        }); 
    }
});'


db.article.positives.find({"sharedList": {$exists: true}, "reconstructions.globalStatus": {$exists: false} , "data.dataUsage": "SHARING"}).forEach(function (a) {
    print('Article: ' + a._id);
    for (i = 0; i<a.sharedList.length; i++){
        db.article.positives.find({"_id":  a.sharedList[i].sharedId}).forEach(function (sharedArticle) {
            a.reconstructions = {};
            if (sharedArticle.reconstructions != null){
                a.reconstructions.globalStatus = sharedArticle.reconstructions.globalStatus;
                db.article.positives.save(a);
            }
        }); 
    }
});