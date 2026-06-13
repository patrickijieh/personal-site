function animate_icon() {
    document.getElementById('icon').animate(
        { transform: ['scale(1) translateY(-150%) translateX(-50%)', 'scale(2.5) translateY(-100%) translateX(-17%)'], opacity: ['100', '0.05'] },
        {
            fill: 'both',
            timeline: new ScrollTimeline({
                source: document.documentElement,
            }),
            rangeStart: CSS.percent(15),
            rangeEnd: CSS.percent(100),
        });
}

function main() {
    animate_icon();
    get_last_updated_date();
}

window.onload = main;
