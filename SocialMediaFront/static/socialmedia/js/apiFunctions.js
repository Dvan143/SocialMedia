function renderLastNews(newsList, containerName) {
    const newsContainer = document.getElementById(containerName);

    if (newsList == null || newsList.length === 0) {
        newsContainer.innerText = 'None';
        newsContainer.style.display = 'flex';
        newsContainer.style.justifyContent = 'center';
        return;
    }

    const maxNews = 10;
    const chunkSize = 5;
    const limitedNewsList = newsList.slice(0, maxNews);

    for (let i = 0; i < limitedNewsList.length; i += chunkSize) {
        const list = document.createElement('ul');
        list.style.marginBottom = '20px';

        const chunk = limitedNewsList.slice(i, i + chunkSize);

        chunk.forEach(news => {
            const currentNews = document.createElement('li');
            currentNews.innerHTML = `
                <h3>${news.title}</h3>
                <p>${news.content}</p>
                <p>Author: ${news.author}</p>
                <p>${news.date}</p>
            `;
            list.appendChild(currentNews);
        });

        newsContainer.appendChild(list);
    }
}
