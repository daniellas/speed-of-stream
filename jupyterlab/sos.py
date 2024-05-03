import json
import pandas as pd
import plotly.express as px

def extract_benchmark_method(benchmark):
    return benchmark.split('.')[-1]

def benchmark_method_selector(excludes,includes):
    if excludes:
        return lambda r: r not in excludes
    if includes:
        return lambda r: r in includes

    return lambda _: True

def load_results(profile, benchmark,excludes=[],includes=[]):
    with open(f'results/{profile}/{benchmark}.json') as file:
        results = json.load(file)
        scores_raw = [(extract_benchmark_method(i['benchmark']),i['params']['size'],i['primaryMetric']['score']) for i in results]
        scores = pd.DataFrame(data=scores_raw,columns=['benchmark','size','score'])
        scores = scores.loc[scores.benchmark.apply(benchmark_method_selector(excludes,includes))]
        max_scores_by_size = scores.groupby(['size']).max()
        scores_pct = scores.join(max_scores_by_size,on='size',rsuffix='_max').drop(columns='benchmark_max')
        scores_pct['score_pct'] = scores_pct['score'].divide(scores_pct['score_max']).multiply(100)
        labels = {'score_pct':'Score %','benchmark':'Benchmark','size':'Items count'}
        chart = px.bar(data_frame = scores_pct,x = 'size', y='score_pct', color='benchmark',barmode='group', labels = labels)
        chart.update_layout(legend=dict(orientation='h',yanchor='bottom',y=1.02,xanchor='left',x=0.01))
        chart.update_layout({'plot_bgcolor':'rgba(0, 0, 0, 0)'})

        return (scores_pct, chart)

def write_chart(chart, profile, benchmark):
    chart.write_image(f'img/{profile}/{benchmark}.png',width='700')

    return chart