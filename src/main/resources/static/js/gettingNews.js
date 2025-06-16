fetch('/socialmedia/api/getLastNews')
    .then(resp => resp.json())
    .then(newsList => {
        renderLastNews(newsList,'newsContainer')
    })
    .catch(error => console.error(`Error of loading: `, error));

fetch('/socialmedia/api/getMyLastNews')
    .then(resp => resp.json())
    .then(newsList => {
        renderLastNews(newsList,'myNewsContainer')
    })
    .catch(error => console.error(`Error of loading: `, error));

function renderLastNews(newsList,containerName){
    const newsContainer = document.getElementById(containerName)
    if(newsList == null || newsList.length === 0){
        newsContainer.innerText='None'
        newsContainer.style.display='flex'
        newsContainer.style.justifyContent='center'
    }
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