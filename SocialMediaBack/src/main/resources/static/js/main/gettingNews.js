// Fetch 5 latest news
fetch('/socialmedia/api/getLastNews')
    .then(resp => resp.json())
    .then(newsList => {
        renderLastNews(newsList,'newsContainer')
    })
    .catch(error => console.error(`Error of loading: `, error));

// Fetch 5 latest news by authorized user
fetch('/socialmedia/api/getMyLastNews')
    .then(resp => resp.json())
    .then(newsList => {
        renderLastNews(newsList,'myNewsContainer')
    })
    .catch(error => console.error(`Error of loading: `, error));