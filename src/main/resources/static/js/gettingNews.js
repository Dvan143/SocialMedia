fetch('/socialmedia/api/getAllNews')
    .then(resp => resp.json())
        .then(newsList => {
            renderNews(newsList)
        })
            .catch(error => console.error(`Error of loading: `, error));
function renderNews(newsList){
    const newsContainer = document.getElementById('newsContainer')
    const list = document.createElement('ul')
    newsList.forEach(news => {
        const currentNews = document.createElement('li')
        currentNews.innerHTML = `
        <h3>${news.title}</h3>
        <p>${news.content}</p>
        <p>Author: ${news.author}</p>
        <p>${news.date}</p>
        `;
        list.appendChild(currentNews)
    });
    newsContainer.appendChild(list)
}