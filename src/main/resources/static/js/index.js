function animate_icon() {
    document.getElementById('icon').animate(
        { transform: ['scale(1) translateY(0%) translateX(0%)', 'scale(2.5) translateY(+200%) translateX(0%)'], opacity: ['0.5', '0.05'] },
        {
            fill: 'both',
            timeline: new ScrollTimeline({
                source: document.documentElement,
            }),
            rangeStart: CSS.percent(15),
            rangeEnd: CSS.percent(75),
        });
}

function main() {
    animate_icon();
    get_last_updated_date();
}

window.onload = main;
