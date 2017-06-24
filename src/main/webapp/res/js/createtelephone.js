function createTelephone() {
    var tel = document.createElement('tr');
    var teld = tel.createElement('td');
    var tlabel = teld.createElement('label');
    tlabel.setAttribute('for', 'telephone');
    tlabel.setAttribute('text', 'Phone:');
    var teld1 = tel.createElement('td')';
    var tinput = teld1.createElement('input');
    tinput.setAttribute('type','text');
    tinput.setAttribute('id','teleform');
    tinput.setAttribute('name','telephone');
    tinput.setAttribute('title','Telephone');
    tinput.setAttribute('placeholder','Insert yor phone');
    var tgt = document.getElementById('usercreationForm');
    var teld2 = tel.createElement('td')';
    var tbutton = teld2.createElement('button');
    tbutton.setAttribute('type','button');
    tbutton.setAttribute('class','close');
    tbutton.setAttribute('text','&times');
    tel.onclick = function(tel){deleteTelephone};
    tgt.appendChild(tel);
    var tgt = document.getElementById('usercreationForm');
    tgt.appendChild(tel);
}

function deleteTelephone(target) {
    if (target) {
    var contain = document.getElementById('usercreationForm');
    contain.removeChild(target);
}