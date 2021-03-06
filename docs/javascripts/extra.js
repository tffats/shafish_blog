document.querySelectorAll('.zoom').forEach(item => {
    item.addEventListener('click', function () {
        this.classList.toggle('image-zoom-large');
    })
});

document.querySelectorAll('#english').forEach(item => {
    item.addEventListener('click', function () {
        console.log(item.innerText)
        var sound = document.createElement('audio');
        sound.id = 'audio-player';
        sound.src = 'https://dict.youdao.com/dictvoice?type=2&audio='+item.innerText;
        sound.type = 'audio/mpeg';
        sound.play();
    })
});

document.querySelectorAll('#british').forEach(item => {
    item.addEventListener('click', function () {
        console.log(item.innerText)
        var sound = document.createElement('audio');
        sound.id = 'audio-player';
        sound.src = 'https://dict.youdao.com/dictvoice?type=1&audio='+item.innerText;
        sound.type = 'audio/mpeg';
        sound.play();
    })
});