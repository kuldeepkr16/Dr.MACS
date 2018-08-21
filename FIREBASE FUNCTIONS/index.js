'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.firestore.document("noti/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  const db = admin.database();
  return admin.firestore().collection("noti").doc(notification_id).get().then(queryResult=>{
    //getting name and message sent
    const from_name = queryResult.data().from;
    const from_message = queryResult.data().message;
    const current_token = queryResult.data().token_id;

    //array storing userids
    var uidArray = [];
    //array storing tokenids
    var tokenArray = [];
    var token_id;
    var payload;

        // database reference to tokens
        var ref = db.ref("tokens");

        //converting JSON object to array
        ref.on("value", function(snapshot) {
          var data = snapshot.val();
          for (var prop in data) {
            uidArray.push(data[prop]);
          }
        }, function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });

        //printing each token value
        for(var i = 0; i< uidArray.length; i++){
          //console.log("printing array "+uidArray[i].token_id);
          tokenArray.push(uidArray[i].token_id);
        }

            payload = {
              notification : {
                title : "Message from : " + from_name,
                body : from_message,
                icon : "default",
                click_action: "in.kd.notify.TARGETNOTIFICATION"
              },
              data : {
                dataMessage : from_message,
                fromName : from_name
              }
            };

        tokenArray.forEach(function(value){
          //fetching token id stored one by one
            token_id = value;

            //checking for the sending device token
            if(token_id != current_token){
              //all other devices
                  return admin.messaging().sendToDevice(token_id, payload).then(result=>{
                  console.log("Notification Sent");
                });
              }
              else{
                  console.log("same device");
              }
        });
    });
});

//function to send bloodRequest notification
exports.sendBloodNotification = functions.firestore.document("bloodRequest/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  const db = admin.database();
  return admin.firestore().collection("bloodRequest").doc(notification_id).get().then(queryResult=>{
    //getting name and message sent
    const from_name = queryResult.data().from;
    const blood_type = queryResult.data().selectedBloodType;
    const urgency = queryResult.data().selectedUrgency;
    const phone_no = queryResult.data().phone_no;
    const current_token = queryResult.data().from_token_id;
    const NOW = queryResult.data().NOW;

    //array storing userids
    var uidArray = [];
    //array storing tokenids
    var tokenArray = [];
    var token_id;

    var payload;

        // database reference to tokens
        var ref = db.ref("tokens");

        //converting JSON object to array
        ref.on("value", function(snapshot) {
          var data = snapshot.val();
          for (var prop in data) {
            uidArray.push(data[prop]);
          }
        }, function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });

        //printing each token value
        for(var i = 0; i< uidArray.length; i++){
          //console.log("printing array "+uidArray[i].token_id);
          tokenArray.push(uidArray[i].token_id);
        }
        //create notification
            payload = {
              notification : {
                title : from_name + " Requested Blood Type" ,
                body : blood_type,
                icon : "default",
                click_action: "in.kd.notify.BLOOD.TARGETNOTIFICATION"
              },
              data : {
                fromName : from_name,
                blood_type : blood_type,
                phone_no : phone_no,
                token_id : current_token,
                urgency : urgency,
                NOW : NOW
              }
            };

        tokenArray.forEach(function(value){
          //fetching token id stored one by one
            token_id = value;

            //checking for the sending device token
            if(token_id != current_token){
              //all other devices
                  return admin.messaging().sendToDevice(token_id, payload).then(result=>{
                  console.log("Notification Sent");
                });
              }
              else{
                  console.log("same device");
              }
        });
    });
});


//function to send confirmation for bloodRequest notification
exports.sendConfirmationForBloodNotification = functions.firestore.document("confirmBloodRequest/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  return admin.firestore().collection("confirmBloodRequest").doc(notification_id).get().then(queryResult=>{

    //getting name and phone no. sent
    const from_name = queryResult.data().from;
    const phone_no = queryResult.data().phone_no;
    const token_id = queryResult.data().token_id;

  //create notification
    var payload = {
        notification : {
          title : from_name,
          body : "confirmed your blood request",
          icon : "default",
          click_action: "in.kd.notify.BLOOD.CONFIRM.TARGETNOTIFICATION"
        },
        data : {
          from_Name : from_name,
          phone_no : phone_no
        }
      };
      console.log("sending to this token id"+token_id);
  return admin.messaging().sendToDevice(token_id, payload).then(result=>{
        console.log("Confirmation Sent");
      });
  });
});

//function to send medicine request to medical stores
exports.sendMedicineRequest = functions.firestore.document("mediRequest/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  const db = admin.database();
  return admin.firestore().collection("mediRequest").doc(notification_id).get().then(queryResult=>{
    //getting name and message sent
    const from_name = queryResult.data().from;
    const medi_name = queryResult.data().medi_name;
    const imageURL = queryResult.data().imageURL;
    const current_token = queryResult.data().from_token_id;

    //array storing userids
    var uidArray = [];
    //array storing tokenids
    var tokenArray = [];
    var token_id;
    var payload;

        // database reference to tokens
        var ref = db.ref("meditokens");

        //converting JSON object to array
        ref.on("value", function(snapshot) {
          var data = snapshot.val();
          for (var prop in data) {
            uidArray.push(data[prop]);
          }
        }, function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });

        //printing each token value
        for(var i = 0; i< uidArray.length; i++){
          //console.log("printing array "+uidArray[i].token_id);
          tokenArray.push(uidArray[i].token_id);
        }

            payload = {
              notification : {
                title : "Medicine Name : " + medi_name,
                body : "This Medicine Is Available??",
                icon : "default",
                click_action : "in.kd.notify.MEDICINE.TARGETNOTIFICATION"
              },
              data : {
                from_name : from_name,
                medi_name : medi_name,
                imageURL : imageURL,
                token_id : current_token
              }
            };
            console.log(from_name+medi_name);
        tokenArray.forEach(function(value){
          //fetching token id stored one by one
            token_id = value;

            //checking for the sending device token
            if(token_id != current_token){
              //all other devices
            return admin.messaging().sendToDevice(token_id, payload).then(result=>{
              console.log("Medicine Request Sent");
              });
            }
            else{
                console.log("same device");
              }
        });
    });
});

//function to send confirmation for bloodRequest notification
exports.sendConfirmationForMediRequest = functions.firestore.document("confirmMediRequest/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  return admin.firestore().collection("confirmMediRequest").doc(notification_id).get().then(queryResult=>{

    //getting name and phone no. sent
    const from_name = queryResult.data().from;
    const phone_no = queryResult.data().phone_no;
    const token_id = queryResult.data().token_id;

  //create notification
    var payload = {
        notification : {
          title : from_name,
          body : "confirmed your blood request",
          icon : "default",
          click_action: "in.kd.notify.MEDICINE.CONFIRM.TARGETNOTIFICATION"
        },
        data : {
          from_Name : from_name,
          phone_no : phone_no
        }
      };
      console.log("sending to this token id"+token_id);
  return admin.messaging().sendToDevice(token_id, payload).then(result=>{
        console.log("Confirmation Sent");
      });
  });
});

//function to send appoinment request to hsopitals
exports.sendAppointmentRequest = functions.firestore.document("appointmentRequest/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  const db = admin.database();
  return admin.firestore().collection("appointmentRequest").doc(notification_id).get().then(queryResult=>{
    //getting name and message sent
    const from_name = queryResult.data().from;
    const chosen_date = queryResult.data().date;
    const chosen_time = queryResult.data().time;
    const phone = queryResult.data().phone_no;
    const current_token = queryResult.data().from_token_id;

    //array storing userids
    var uidArray = [];
    //array storing tokenids
    var tokenArray = [];
    var token_id;
    var payload;

        // database reference to tokens
        var ref = db.ref("hospitokens");

        //converting JSON object to array
        ref.on("value", function(snapshot) {
          var data = snapshot.val();
          for (var prop in data) {
            uidArray.push(data[prop]);
          }
        }, function (errorObject) {
          console.log("The read failed: " + errorObject.code);
        });

        //printing each token value
        for(var i = 0; i< uidArray.length; i++){
          //console.log("printing array "+uidArray[i].token_id);
          tokenArray.push(uidArray[i].token_id);
        }

            payload = {
              notification : {
                title : "Appointment request from : ",
                body : from_name,
                icon : "default",
                click_action : "in.kd.notify.APPOINTMENT.TARGETNOTIFICATION"
              },
              data : {
                from_name : from_name,
                chosen_date : chosen_date,
                chosen_time : chosen_time,
                token_id : current_token,
                phone : phone
              }
            };
            console.log(from_name+chosen_time+chosen_date);
        tokenArray.forEach(function(value){
          //fetching token id stored one by one
            token_id = value;

            //checking for the sending device token
            if(token_id != current_token){
              //all other devices
            return admin.messaging().sendToDevice(token_id, payload).then(result=>{
              console.log("Appoinment Request Sent");
              });
            }
            else{
                console.log("same device");
              }
        });
    });
});

//function to send confirmation for bloodRequest notification
exports.sendConfirmationForMediRequest = functions.firestore.document("confirmAppoinRequest/{notification_id}").onWrite(event=>{

  const notification_id = event.params.notification_id;
  return admin.firestore().collection("confirmAppoinRequest").doc(notification_id).get().then(queryResult=>{

    //getting name and phone no. sent
    const from_name = queryResult.data().from;
    const phone_no = queryResult.data().phone_no;
    const token_id = queryResult.data().token_id;

  //create notification
    var payload = {
        notification : {
          title : from_name,
          body : "confirmed your appointment request",
          icon : "default",
          click_action: "in.kd.notify.APPOINTMENT.CONFIRM.TARGETNOTIFICATION"
        },
        data : {
          from_Name : from_name,
          phone_no : phone_no
        }
      };
      console.log("sending to this token id"+token_id);
  return admin.messaging().sendToDevice(token_id, payload).then(result=>{
        console.log("Confirmation Sent");
      });
  });
});
