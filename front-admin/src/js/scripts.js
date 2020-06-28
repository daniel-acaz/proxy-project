function run() {
    fetch('http://localhost:8081/requests/most').then(data=>{return data.json()}).then(res=>{updateCards(res)})
    fetch('http://localhost:8080/parameters').then(data=>{return data.json()}).then(res=>{populateParameters(res)})
}

function updateCards(res) {

    document.getElementById("ip").innerHTML = res.originIp.type ? res.originIp.type : 'N/A'
    document.getElementById("ipTimes").innerHTML = res.originIp.amount ? res.originIp.amount : 'N/A'

    document.getElementById("target").innerHTML = res.targetPath.type ? res.targetPath.type : 'N/A'
    document.getElementById("targetTimes").innerHTML = res.targetPath.amount ? res.targetPath.amount : 'N/A'

    document.getElementById("bothIp").innerHTML = res.both.originIp ? res.both.originIp : 'N/A'
    document.getElementById("bothTarget").innerHTML = res.both.targetPah ? res.both.targetPah : 'N/A'
    document.getElementById("bothTimes").innerHTML = res.both.amount ? res.both.amount : 'N/A'

}

function populateParameters(res) {

    document.getElementById("ipLimit").value = res.limitOrigin
    document.getElementById("targetLimit").value = res.limitTarget
    document.getElementById("bothLimit").value = res.limitBoth
    document.getElementById("timeExpiration").value = res.expirationTime

}

function updateParameters() {

    var ip = document.getElementById("ipLimit").value
    var target = document.getElementById("targetLimit").value
    var both = document.getElementById("bothLimit").value
    var expiration = document.getElementById("timeExpiration").value

    var data = {
        "limitOrigin": ip,
        "limitTarget": target,
        "limitBoth": both,
        "expirationTime": expiration
    }

    putRequest(data)

}

function putRequest(data) {

    fetch('http://localhost:8080/parameters', {

        method: 'PUT',
        body: JSON.stringify(data),
        headers: new Headers({
            'Content-Type': 'application/json'

        }),
    })
        .then(response => console.log(response.json()))
}

function testRequest(event) {

    event.preventDefault();
    event.stopImmediatePropagation();

    var url = document.getElementById("url").value

    var myHeaders = new Headers({
        'Cache-Control': 'no-cache',
        'Accept':'*/*',
        'Accept-Encoding':'gzip, deflate, br',
        'Connection':'keep-alive'
    });

    fetch('http://localhost:8080/api/' + url, {
        method: 'GET',
        headers: myHeaders
    })
        .then(response => { getStatus(response) })

}

const button = document.getElementById('submit');
button.addEventListener('click', testRequest);

function getStatus(response) {

    if (response.status == 200) {
        document.getElementById("status").innerHTML = "Status 200 OK!"
        document.getElementById("status").style.color = "green"
    } else if (response.status == 403) {
        document.getElementById("status").innerHTML = "Status 403 Forbidden!"
        document.getElementById("status").style.color = "red"
    } else {
        document.getElementById("status").innerHTML = response.status + " Something wrong!"
        document.getElementById("status").style.color = "red"
    }

    fetch('http://localhost:8081/requests/most').then(data=>{return data.json()}).then(res=>{updateCards(res)})

}

run();

