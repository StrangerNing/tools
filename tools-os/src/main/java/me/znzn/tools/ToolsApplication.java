package me.znzn.tools;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ToolsApplication extends SpringBootServletInitializer {
	@Value("${lucene.directory.path}")
	private String path;

	public static void main(String[] args) {
		SpringApplication.run(ToolsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ToolsApplication.class);
	}

	@Bean
	public TaskScheduler taskScheduler(){

		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.initialize();
		return taskScheduler;
	}

	@Bean
	public Directory directory() {
		try {
			Path path = Paths.get(this.path);
			return FSDirectory.open(path);
		}catch (IOException io) {
			log.error("读取lucene索引失败，io异常", io);
		}
		return null;
	}

	@Bean
	public Analyzer analyzer() {
		//创建SegmenterConfig分词配置实例，自动查找加载jcseg.properties配置项来初始化
		SegmenterConfig config = new SegmenterConfig(ResourceUtil.getStream("jcseg.properties"));

		//创建默认单例词库实现，并且按照config配置加载词库
		ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
		return new JcsegAnalyzer(ISegment.NLP, config, dic);
	}
}
