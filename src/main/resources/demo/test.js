const password = "SuperSecret123!";
const apiKey = "sk_live_abcdef1234567890";

function run(userCmd) {
    // SG004
    exec(userCmd);
    execSync(userCmd);
}

function readFile(path) {
    // SG005
    fs.readFileSync(path);
}

function weakHash(data) {
    // SG006
    return crypto.createHash('md5');
}

function genToken() {
    // SG007
    return Math.random();
}

function callApi() {
    // SG010
    return fetch("http://internal-api.example.com/data");
}

console.log("NODE_ENV=development");
